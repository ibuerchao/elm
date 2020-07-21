package com.buerc.permission.service.impl;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.constants.SysConstant;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.TreeNode;
import com.buerc.common.utils.TreeUtil;
import com.buerc.common.utils.ValidateKit;
import com.buerc.common.web.Result;
import com.buerc.permission.config.WebLogAspect;
import com.buerc.permission.mapper.SysPermissionModuleMapper;
import com.buerc.permission.model.SysPermissionModule;
import com.buerc.permission.model.SysPermissionModuleExample;
import com.buerc.permission.service.SysResModuleService;
import com.buerc.sys.dto.ResListParam;
import com.buerc.sys.dto.ResModuleFormParam;
import com.buerc.sys.dto.UpdateStatusParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SysResModuleServiceImpl implements SysResModuleService {

    @Resource
    private SysPermissionModuleMapper sysPermissionModuleMapper;

    @Override
    public SysPermissionModule checkIdExist(String id) {
        ValidateKit.notBlank(id, ResultCode.RES_MODULE_ID_BLANK_MSG);
        SysPermissionModule module = sysPermissionModuleMapper.selectByPrimaryKey(id);
        if (module == null) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.RES_MODULE_NOT_EXIST_MSG);
        }
        return module;
    }

    @Override
    public SysPermissionModule add(ResModuleFormParam param) {
        checkNameIsRepeat(null,param.getParentId(),param.getName());
        checkParentIdIsExist(param.getParentId());
        Pair<String, Integer> nextCode = getNextCode(param.getParentId());
        SysPermissionModule module = new SysPermissionModule();
        BeanUtils.copyProperties(param,module);
        module.setLevel(nextCode.getLeft());
        module.setSeq(nextCode.getRight());
        sysPermissionModuleMapper.insertSelective(module);
        return module;
    }

    /**
     * 判断名称是否重复
     */
    private void checkNameIsRepeat(String id, String parentId, String name) {
        SysPermissionModuleExample example = new SysPermissionModuleExample();
        example.createCriteria().andNameEqualTo(name).andParentIdEqualTo(parentId);
        List<SysPermissionModule> list = sysPermissionModuleMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            if (StringUtils.isBlank(id)) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.RES_MODULE_NAME_REPEAT_MSG);
            } else {
                List<String> collect = list.stream().map(SysPermissionModule::getId).collect(Collectors.toList());
                if (!collect.contains(id)) {
                    throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.RES_MODULE_NAME_REPEAT_MSG);
                }
            }
        }
    }

    /**
     * 验证父级ID是否存在
     */
    private void checkParentIdIsExist(String parentId) {
        if (!SysConstant.Sys.DEFAULT_RES_MODULE_ID.equals(parentId)) {
            SysPermissionModule module = sysPermissionModuleMapper.selectByPrimaryKey(parentId);
            ValidateKit.notNull(module, ResultCode.PARENT_MODULE_NOT_EXIST_MSG);
        }
    }

    private Pair<String,Integer> getNextCode(String parentId){
        SysPermissionModuleExample example = new SysPermissionModuleExample();
        example.setOrderByClause("seq desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        example.setOffset(SysConstant.OFFSET);
        example.setLimit(SysConstant.LIMIT);
        List<SysPermissionModule> modules = sysPermissionModuleMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(modules)) {
            SysPermissionModule module = modules.get(0);
            return Pair.of(getNextLevel(parentId,module),getNextSeq(module));
        } else {
            return Pair.of(getNextLevel(parentId,null),getNextSeq(null));
        }
    }

    private String getNextLevel(String moduleId,SysPermissionModule module){
        if (Objects.isNull(module)){
            if (SysConstant.Sys.DEFAULT_RES_MODULE_ID.equals(moduleId)){
                return getCurrentLevel(1);
            }else{
                SysPermissionModule permissionModule = checkIdExist(moduleId);
                return permissionModule.getLevel().concat(getCurrentLevel(1));
            }
        }else{
            if (SysConstant.Sys.DEFAULT_RES_MODULE_ID.equals(moduleId)){
                int nextLevel = NumberUtils.toInt(module.getLevel()) + 1;
                return getCurrentLevel(nextLevel);
            }else{
                String previousLevel = module.getLevel();
                String patentLevel = previousLevel.substring(0, previousLevel.length() - SysConstant.LEVEL_LENGTH);
                String siblingLevel = previousLevel.substring(previousLevel.length() - SysConstant.LEVEL_LENGTH);
                int nextLevel = NumberUtils.toInt(siblingLevel) + 1;
                return patentLevel.concat(getCurrentLevel(nextLevel));
            }
        }
    }

    private String getCurrentLevel(int level){
        if (level>=Math.pow(10,SysConstant.LEVEL_LENGTH)){
            throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.RES_MODULE_MAX_MSG);
        }
        return StringUtils.leftPad(String.valueOf(level),SysConstant.LEVEL_LENGTH,SysConstant.PADDING);
    }

    private Integer getNextSeq(SysPermissionModule module) {
        if (Objects.isNull(module)) {
            return 1;
        } else {
            return module.getSeq() + 1;
        }
    }

    @Override
    public void delete(String id) {
        SysPermissionModule module = checkIdExist(id);
        ValidateKit.assertTrue(SysConstant.ResModuleStatus.NORMAL.equals(module.getStatus()),ResultCode.FORBID_DELETE_RES_MODULE_MSG);
        List<SysPermissionModule> list = new ArrayList<>();
        childrenList(list, id, Boolean.FALSE);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.MODULE_CANNOT_DEL_MSG);
        }
        sysPermissionModuleMapper.deleteByPrimaryKey(id);
        WebLogAspect.fillTextValue(module,null);
    }

    /**
     * 是否迭代获取当前节点的子节点
     */
    private void childrenList(List<SysPermissionModule> list, String id, boolean flag) {
        SysPermissionModuleExample example = new SysPermissionModuleExample();
        example.createCriteria().andParentIdEqualTo(id);
        List<SysPermissionModule> modules = sysPermissionModuleMapper.selectByExample(example);
        list.addAll(modules);
        if (flag) {
            if (CollectionUtils.isNotEmpty(modules)) {
                for (SysPermissionModule module : modules) {
                    childrenList(list, module.getId(), Boolean.TRUE);
                }
            }
        }
    }

    @Override
    public SysPermissionModule edit(ResModuleFormParam param) {
        //todo 更新seq和level
        SysPermissionModule module = checkIdExist(param.getId());
        checkNameIsRepeat(param.getId(), param.getParentId(), param.getName());
        checkParentIdIsExist(param.getParentId());
        checkHierarchicalRelationship(param.getId(), param.getParentId());
        SysPermissionModule update = new SysPermissionModule();
        BeanUtils.copyProperties(param, update);
        sysPermissionModuleMapper.updateByPrimaryKeySelective(update);
        WebLogAspect.fillTextValue(module,update);
        return update;
    }

    /**
     * 检查层级关系(不允许将父级部门挪到其子级部门下)
     */
    private void checkHierarchicalRelationship(String id, String parentId) {
        ValidateKit.assertTrue(StringUtils.equals(id,parentId),ResultCode.FORBID_MODULE_TO_SELF_MSG);
        List<SysPermissionModule> list = new ArrayList<>();
        childrenList(list, id, Boolean.TRUE);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> collect = list.stream().map(SysPermissionModule::getId).collect(Collectors.toList());
            if (collect.contains(parentId) || collect.contains(id)) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.FORBID_MODULE_TO_CHILD_MSG);
            }
        }
    }

    @Override
    public SysPermissionModule detail(String id) {
        return checkIdExist(id);
    }

    @Override
    public boolean updateStatus(UpdateStatusParam param) {
        checkIdExist(param.getId());
        checkStatus(param.getStatus());
        SysPermissionModule module = new SysPermissionModule();
        module.setId(param.getId());
        module.setStatus(param.getStatus());
        int update = sysPermissionModuleMapper.updateByPrimaryKeySelective(module);
        return update > 0;
    }
    private void checkStatus(Byte status){
        boolean result = SysConstant.ResModuleStatus.FORBID.equals(status) || SysConstant.ResModuleStatus.NORMAL.equals(status);
        ValidateKit.assertTrue(!result,ResultCode.MODULE_STATUS_INVALID_MSG);
    }

    @Override
    public Result<List<SysPermissionModule>> list(ResListParam param) {
        return null;
    }

    /**
     * 查询当前节点的模块树
     */
    @Override
    public List<TreeNode> superior(String id, String status) {
        Integer s = NumberUtils.toInt(status, -1);
        if (s.equals(-1)) {
            s = null;
        }
        List<TreeNode> data = sysPermissionModuleMapper.tree(s);
        return TreeUtil.tree(data,id);
    }
}
