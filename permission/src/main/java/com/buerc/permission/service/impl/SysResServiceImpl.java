package com.buerc.permission.service.impl;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.constants.SysConstant;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.ValidateKit;
import com.buerc.common.web.Result;
import com.buerc.permission.config.WebLogAspect;
import com.buerc.permission.mapper.SysPermissionMapper;
import com.buerc.permission.model.SysPermission;
import com.buerc.permission.model.SysPermissionExample;
import com.buerc.permission.service.SysResModuleService;
import com.buerc.permission.service.SysResService;
import com.buerc.sys.dto.ResFormParam;
import com.buerc.sys.dto.ResListParam;
import com.buerc.sys.dto.UpdateStatusParam;
import com.buerc.sys.vo.ResVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysResServiceImpl implements SysResService {

    @Resource
    private SysResModuleService sysResModuleService;

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public SysPermission add(ResFormParam param) {
        checkModuleIdIsExist(param.getModuleId());
        checkNameIsRepeat(null, param.getModuleId(), param.getName());
        checkCodeIsRepeat(null, param.getModuleId(), param.getCode());
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(param,sysPermission);
        sysPermission.setSeq(getNextSeq(sysPermission.getModuleId()));
        sysPermissionMapper.insertSelective(sysPermission);
        WebLogAspect.fillTextValue(null,sysPermission);
        return sysPermission;
    }

    /**
     * 判断名称是否重复
     */
    private void checkNameIsRepeat(String id, String parentId, String name) {
        SysPermissionExample example = new SysPermissionExample();
        example.createCriteria().andNameEqualTo(name).andModuleIdEqualTo(parentId);
        List<SysPermission> list = sysPermissionMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            if (StringUtils.isBlank(id)) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.RES_NAME_REPEAT_MSG);
            } else {
                List<String> collect = list.stream().map(SysPermission::getId).collect(Collectors.toList());
                if (!collect.contains(id)) {
                    throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.RES_NAME_REPEAT_MSG);
                }
            }
        }
    }

    /**
     * 判断编码是否重复
     */
    private void checkCodeIsRepeat(String id, String parentId, String code) {
        SysPermissionExample example = new SysPermissionExample();
        example.createCriteria().andCodeEqualTo(code).andModuleIdEqualTo(parentId);
        List<SysPermission> list = sysPermissionMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            if (StringUtils.isBlank(id)) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.RES_CODE_REPEAT_MSG);
            } else {
                List<String> collect = list.stream().map(SysPermission::getId).collect(Collectors.toList());
                if (!collect.contains(id)) {
                    throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.RES_CODE_REPEAT_MSG);
                }
            }
        }
    }

    /**
     * 查询当前模块下最大的seq
     */
    private Integer getNextSeq(String moduleId) {
        SysPermissionExample example = new SysPermissionExample();
        example.setOrderByClause("seq desc");
        example.createCriteria().andModuleIdEqualTo(moduleId);
        example.setOffset(SysConstant.OFFSET);
        example.setLimit(SysConstant.LIMIT);
        List<SysPermission> permissions = sysPermissionMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(permissions)) {
            return permissions.get(0).getSeq() + 1;
        } else {
            return 1;
        }
    }

    @Override
    @Transactional
    public void delete(String id) {
        SysPermission permission = checkIdExist(id);
        ValidateKit.assertTrue(SysConstant.ResStatus.NORMAL.equals(permission.getStatus()),ResultCode.FORBID_DELETE_RES_MSG);
        sysPermissionMapper.deleteByPrimaryKey(id);
        WebLogAspect.fillTextValue(permission,null);
    }

    /**
     * 检测id是否合法
     */
    private SysPermission checkIdExist(String id) {
        ValidateKit.notBlank(id, ResultCode.RES_ID_BLANK_MSG);
        SysPermission sysPermission = sysPermissionMapper.selectByPrimaryKey(id);
        if (sysPermission == null) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.RES_NOT_EXIST_MSG);
        }
        return sysPermission;
    }

    @Override
    public SysPermission edit(ResFormParam param) {
        SysPermission permission = checkIdExist(param.getId());
        checkModuleIdIsExist(param.getModuleId());
        checkNameIsRepeat(param.getId(), param.getModuleId(), param.getName());
        checkCodeIsRepeat(param.getId(), param.getModuleId(), param.getCode());
        SysPermission update = new SysPermission();
        BeanUtils.copyProperties(param, update);
        sysPermissionMapper.updateByPrimaryKeySelective(update);
        WebLogAspect.fillTextValue(permission,update);
        return update;
    }

    private void checkModuleIdIsExist(String moduleId){
        sysResModuleService.checkIdExist(moduleId);
    }

    @Override
    public SysPermission detail(String id) {
        return checkIdExist(id);
    }

    @Override
    public boolean updateStatus(UpdateStatusParam param) {
        checkIdExist(param.getId());
        checkStatus(param.getStatus());
        SysPermission sysPermission = new SysPermission();
        sysPermission.setId(param.getId());
        sysPermission.setStatus(param.getStatus());
        int update = sysPermissionMapper.updateByPrimaryKeySelective(sysPermission);
        return update > 0;
    }

    private void checkStatus(Byte status){
        boolean result = SysConstant.ResStatus.FORBID.equals(status) || SysConstant.ResStatus.NORMAL.equals(status);
        ValidateKit.assertTrue(!result,ResultCode.DEPT_STATUS_INVALID_MSG);
    }

    @Override
    public Result<List<ResVo>> list(ResListParam param) {
        if (param.getEnd() != null && param.getStart() != null) {
            ValidateKit.assertTrue(param.getEnd().compareTo(param.getStart()) < 0, ResultCode.START_AND_END_INVALID_MSG);
        }
        return Result.success(sysPermissionMapper.list(param), sysPermissionMapper.count(param));
    }
}
