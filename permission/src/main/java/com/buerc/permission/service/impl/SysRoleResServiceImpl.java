package com.buerc.permission.service.impl;

import com.buerc.CodeUtil;
import com.buerc.common.constants.ResultCode;
import com.buerc.common.constants.SysConstant;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.DateUtil;
import com.buerc.common.web.Result;
import com.buerc.permission.enums.CodeConfigEnum;
import com.buerc.permission.mapper.SysDeptMapper;
import com.buerc.permission.mapper.SysPermissionMapper;
import com.buerc.permission.mapper.SysPermissionModuleMapper;
import com.buerc.permission.mapper.SysRolePermissionMapper;
import com.buerc.permission.model.*;
import com.buerc.permission.service.SysRoleResService;
import com.buerc.permission.service.SysRoleService;
import com.buerc.permission.service.SysRoleUserService;
import com.buerc.redis.Event;
import com.buerc.redis.MessageProcessor;
import com.buerc.redis.constants.EventConstants;
import com.buerc.sys.dto.RoleResFormParam;
import com.buerc.sys.dto.RoleResListParam;
import com.buerc.sys.vo.TransferVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysRoleResServiceImpl implements SysRoleResService {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleUserService sysRoleUserService;

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysPermissionModuleMapper sysPermissionModuleMapper;

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private MessageProcessor messageProcessor;

    @Override
    public void save(RoleResFormParam param) {
        sysRoleService.detail(param.getRoleId());
        check(param);
        delete(param);
        insert(param);
    }

    private void check(RoleResFormParam param){
        if (SysConstant.RoleResTargetType.ORG.equals(param.getTargetType())) {
            checkOrg(param.getTargetIds());
        } else if (SysConstant.RoleResTargetType.MODULE.equals(param.getTargetType())) {
            checkModule(param.getTargetIds());
        } else {
            checkRes(param.getTargetIds());
        }
    }

    private void checkOrg(Set<String> targetIds) {
        if (CollectionUtils.isNotEmpty(targetIds)){
            SysDeptExample example = new SysDeptExample();
            example.createCriteria().andIdIn(new ArrayList<>(targetIds));
            List<SysDept> list = sysDeptMapper.selectByExample(example);
            if (list.size() != targetIds.size()) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.CONTAIN_NOT_EXIST_DEPT_MSG);
            }
        }
    }

    private void checkModule(Set<String> targetIds) {
        if (CollectionUtils.isNotEmpty(targetIds)){
            SysPermissionModuleExample example = new SysPermissionModuleExample();
            example.createCriteria().andIdIn(new ArrayList<>(targetIds));
            List<SysPermissionModule> list = sysPermissionModuleMapper.selectByExample(example);
            if (list.size() != targetIds.size()) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.CONTAIN_NOT_EXIST_MODULE_MSG);
            }
        }
    }

    private void checkRes(Set<String> targetIds) {
        if (CollectionUtils.isNotEmpty(targetIds)){
            SysPermissionExample example = new SysPermissionExample();
            example.createCriteria().andIdIn(new ArrayList<>(targetIds));
            List<SysPermission> list = sysPermissionMapper.selectByExample(example);
            if (list.size() != targetIds.size()) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.CONTAIN_NOT_EXIST_RES_MSG);
            }
        }
    }

    private void delete(RoleResFormParam param){
        SysRolePermissionExample example = new SysRolePermissionExample();
        example.createCriteria().andRoleIdEqualTo(param.getRoleId()).andTargetTypeEqualTo(param.getTargetType());
        sysRolePermissionMapper.deleteByExample(example);
    }

    private void insert(RoleResFormParam param){
        Set<String> targetIds = param.getTargetIds();
        if (CollectionUtils.isNotEmpty(targetIds)){
            List<SysRolePermission> list = new ArrayList<>();
            for (String targetId:targetIds){
                SysRolePermission sysRolePermission = new SysRolePermission();
                sysRolePermission.setId(CodeUtil.getCode(CodeConfigEnum.ROLE_RES.getKey(), DateUtil.formatShortCompact()));
                sysRolePermission.setRoleId(param.getRoleId());
                sysRolePermission.setTargetId(targetId);
                sysRolePermission.setTargetType(param.getTargetType());
                list.add(sysRolePermission);
            }
            sysRolePermissionMapper.insertBatch(list);
        }
    }

    @Override
    public Result list(RoleResListParam param) {
        SysRolePermissionExample example = new SysRolePermissionExample();
        example.createCriteria().andRoleIdEqualTo(param.getRoleId()).andTargetTypeEqualTo(param.getTargetType());
        List<SysRolePermission> list = sysRolePermissionMapper.selectByExample(example);
        Set<String> set = list.stream().map(SysRolePermission::getTargetId).collect(Collectors.toSet());
        if (SysConstant.RoleResTargetType.RES.equals(param.getTargetType())){
            TransferVo vo = new TransferVo();
            List<SysPermission> permissions = sysPermissionMapper.selectByExample(new SysPermissionExample());
            vo.setId(param.getRoleId());

            List<TransferVo.TransferData> l = new ArrayList<>();
            for (SysPermission permission:permissions){
                TransferVo.TransferData data = new TransferVo.TransferData();
                data.setKey(permission.getId());
                data.setLabel(permission.getName());
                data.setDisabled(!SysConstant.ResStatus.NORMAL.equals(permission.getStatus()));
                l.add(data);
            }
            vo.setData(l);
            vo.setValue(set);
            return Result.success(vo);
        }else{
            return Result.success(set);
        }
    }

    @Override
    public void publish(RoleResFormParam param) {
        Event<RoleResFormParam> event = new Event<>();
        event.setTopic(messageProcessor.getTopic());
        event.setModule(EventConstants.Module.WEB_SYS);
        event.setType(EventConstants.Type.ROLE_RES);

        List<SysRoleUser> roleUsers = sysRoleUserService.getSysRoleUserByRoleId(param.getRoleId());
        Set<String> userIds = roleUsers.stream().map(SysRoleUser::getUserId).collect(Collectors.toSet());
        param.setUserIds(userIds);
        event.setData(param);
        messageProcessor.publish(event);
    }
}
