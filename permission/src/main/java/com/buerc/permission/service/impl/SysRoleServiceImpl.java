package com.buerc.permission.service.impl;

import com.buerc.CodeUtil;
import com.buerc.common.constants.ResultCode;
import com.buerc.common.constants.SysConstant;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.DateUtil;
import com.buerc.common.utils.ValidateKit;
import com.buerc.common.web.Result;
import com.buerc.permission.enums.CodeConfigEnum;
import com.buerc.permission.mapper.SysRoleMapper;
import com.buerc.permission.mapper.SysRolePermissionMapper;
import com.buerc.permission.mapper.SysRoleUserMapper;
import com.buerc.permission.model.*;
import com.buerc.permission.service.SysRoleService;
import com.buerc.redis.Event;
import com.buerc.redis.MessageProcessor;
import com.buerc.redis.constants.EventConstants;
import com.buerc.sys.dto.RoleFormParam;
import com.buerc.sys.dto.RoleListParam;
import com.buerc.sys.dto.UpdateStatusParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private MessageProcessor messageProcessor;

    @Override
    public SysRole add(RoleFormParam param) {
        validateCode(param.getCode());
        SysRole role = new SysRole();
        BeanUtils.copyProperties(param,role);
        role.setId(CodeUtil.getCode(CodeConfigEnum.ROLE.getKey(), DateUtil.formatShortCompact()));
        sysRoleMapper.insertSelective(role);
        return role;
    }

    private void validateCode(String code) {
        SysRoleExample example = new SysRoleExample();
        example.createCriteria().andCodeEqualTo(code);
        long count = sysRoleMapper.countByExample(example);
        if (count > 0) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.ROLE_CODE_EXIST_ERROR_MSG);
        }
    }

    @Override
    public Set<String> delete(String id) {
        ValidateKit.assertTrue(StringUtils.isBlank(id),ResultCode.PARAM_ERROR_MSG);
        SysRole role = checkIdExist(id);
        ValidateKit.assertTrue(SysConstant.RoleStatus.NORMAL.equals(role.getStatus()),ResultCode.ROLE_STATUS_NORMAL_MSG);
        sysRoleMapper.deleteByPrimaryKey(id);

        //删除角色关联的资源信息与用户信息
        SysRolePermissionExample sysRolePermissionExample = new SysRolePermissionExample();
        sysRolePermissionExample.createCriteria().andRoleIdEqualTo(id);
        sysRolePermissionMapper.deleteByExample(sysRolePermissionExample);

        SysRoleUserExample sysRoleUserExample = new SysRoleUserExample();
        sysRoleUserExample.createCriteria().andRoleIdEqualTo(id);
        List<SysRoleUser> roleUsers = sysRoleUserMapper.selectByExample(sysRoleUserExample);
        sysRoleUserMapper.deleteByExample(sysRoleUserExample);

        return roleUsers.stream().map(SysRoleUser::getUserId).collect(Collectors.toSet());
    }

    private SysRole checkIdExist(String id){
        SysRole role = sysRoleMapper.selectByPrimaryKey(id);
        if (role == null) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.ROLE_NOT_EXIST_ERROR_MSG);
        }
        return role;
    }

    @Override
    public Pair<SysRole,Boolean> edit(RoleFormParam param) {
        ValidateKit.assertTrue(StringUtils.isBlank(param.getId()),ResultCode.PARAM_ERROR_MSG);
        SysRole role = checkIdExist(param.getId());
        if (!role.getCode().equals(param.getCode())){
            validateCode(param.getCode());
        }
        SysRole update = new SysRole();
        BeanUtils.copyProperties(param,update);
        sysRoleMapper.updateByPrimaryKeySelective(update);
        return Pair.of(update,role.getStatus().equals(param.getStatus()));
    }

    @Override
    public SysRole detail(String id) {
        ValidateKit.assertTrue(StringUtils.isBlank(id),ResultCode.PARAM_ERROR_MSG);
        return checkIdExist(id);
    }

    @Override
    public boolean updateStatus(UpdateStatusParam param) {
        checkStatus(param.getStatus());
        checkIdExist(param.getId());
        SysRole role = new SysRole();
        role.setId(param.getId());
        role.setStatus(param.getStatus());
        int update = sysRoleMapper.updateByPrimaryKeySelective(role);
        return update > 0;
    }

    private void checkStatus(Byte status){
        ArrayList<Byte> list = new ArrayList<>();
        list.add(SysConstant.RoleStatus.FORBID);
        list.add(SysConstant.RoleStatus.NORMAL);
        ValidateKit.assertTrue(!list.contains(status),ResultCode.ROLE_STATUS_INVALID_MSG);
    }

    @Override
    public Result<List<SysRole>> list(RoleListParam param) {
        if (param.getEnd() != null && param.getStart() != null) {
            ValidateKit.assertTrue(param.getEnd().compareTo(param.getStart()) < 0, ResultCode.START_AND_END_INVALID_MSG);
        }
        return Result.success(sysRoleMapper.list(param), sysRoleMapper.count(param));
    }

    @Override
    public void publish(Set<String> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)){
            Event<Set<String>> event = new Event<>();
            event.setTopic(messageProcessor.getTopic());
            event.setModule(EventConstants.Module.WEB_SYS);
            event.setType(EventConstants.Type.ROLE_CHANGE);
            event.setData(userIds);
            messageProcessor.publish(event);
        }
    }

    @Override
    public void publish(String roleId) {
        SysRoleUserExample sysRoleUserExample = new SysRoleUserExample();
        sysRoleUserExample.createCriteria().andRoleIdEqualTo(roleId);
        List<SysRoleUser> roleUsers = sysRoleUserMapper.selectByExample(sysRoleUserExample);
        Set<String> userIds = roleUsers.stream().map(SysRoleUser::getUserId).collect(Collectors.toSet());
        publish(userIds);
    }
}
