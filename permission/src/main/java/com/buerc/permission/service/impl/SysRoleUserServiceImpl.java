package com.buerc.permission.service.impl;

import com.buerc.CodeUtil;
import com.buerc.common.constants.ResultCode;
import com.buerc.common.constants.SysConstant;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.DateUtil;
import com.buerc.permission.enums.CodeConfigEnum;
import com.buerc.permission.mapper.SysRoleMapper;
import com.buerc.permission.mapper.SysRoleUserMapper;
import com.buerc.permission.mapper.SysUserMapper;
import com.buerc.permission.model.*;
import com.buerc.permission.service.SysRoleUserService;
import com.buerc.redis.Event;
import com.buerc.redis.MessageProcessor;
import com.buerc.redis.constants.EventConstants;
import com.buerc.sys.dto.RoleUserFormParam;
import com.buerc.sys.dto.RoleUserListParam;
import com.buerc.sys.vo.TransferVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysRoleUserServiceImpl implements SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private MessageProcessor messageProcessor;

    @Override
    public void save(RoleUserFormParam param) {
        check(param);
        delete(param);
        insert(param);
    }

    private void check(RoleUserFormParam param){
        if (SysConstant.RoleUserTargetType.ROLE_USER.equals(param.getType())){
            SysRoleExample example = new SysRoleExample();
            example.createCriteria().andIdEqualTo(param.getId());
            List<SysRole> roles = sysRoleMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(roles)) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.ROLE_NOT_EXIST_ERROR_MSG);
            }
            checkUser(param.getTargetIds());
        }else{
            SysUserExample example = new SysUserExample();
            example.createCriteria().andIdEqualTo(param.getId());
            List<SysUser> users = sysUserMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(users)) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.USER_NOT_EXIST_ERROR_MSG);
            }
            checkRole(param.getTargetIds());
        }
    }

    private void checkRole(Set<String> roleIds) {
        if (CollectionUtils.isNotEmpty(roleIds)){
            SysRoleExample example = new SysRoleExample();
            example.createCriteria().andIdIn(new ArrayList<>(roleIds));
            List<SysRole> roles = sysRoleMapper.selectByExample(example);
            if (roles.size() != roleIds.size()) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.CONTAIN_NOT_EXIST_ROLE_MSG);
            }
        }
    }

    private void checkUser(Set<String> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)){
            SysUserExample example = new SysUserExample();
            example.createCriteria().andIdIn(new ArrayList<>(userIds));
            List<SysUser> users = sysUserMapper.selectByExample(example);
            if (users.size() != userIds.size()) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.CONTAIN_NOT_EXIST_USER_MSG);
            }
        }
    }

    private void delete(RoleUserFormParam param) {
        SysRoleUserExample example = new SysRoleUserExample();
        SysRoleUserExample.Criteria criteria = example.createCriteria();
        if (SysConstant.RoleUserTargetType.ROLE_USER.equals(param.getType())){
            criteria.andRoleIdEqualTo(param.getId());
        }else{
            criteria.andUserIdEqualTo(param.getId());
        }
        sysRoleUserMapper.deleteByExample(example);
    }

    private void insert(RoleUserFormParam param){
        if (CollectionUtils.isNotEmpty(param.getTargetIds())){
            List<SysRoleUser> list = new ArrayList<>();
            for (String id:param.getTargetIds()){
                SysRoleUser sysRoleUser = new SysRoleUser();
                sysRoleUser.setId(CodeUtil.getCode(CodeConfigEnum.ROLE_USER.getKey(), DateUtil.formatShortCompact()));
                if (SysConstant.RoleUserTargetType.ROLE_USER.equals(param.getType())){
                    sysRoleUser.setUserId(id);
                    sysRoleUser.setRoleId(param.getId());
                }else{
                    sysRoleUser.setUserId(param.getId());
                    sysRoleUser.setRoleId(id);
                }
                list.add(sysRoleUser);
            }
            sysRoleUserMapper.insertBatch(list);
        }
    }

    @Override
    public TransferVo list(RoleUserListParam param) {
        if (SysConstant.RoleUserTargetType.ROLE_USER.equals(param.getType())){
            return listByRole(param);
        }else {
            return listByUser(param);
        }
    }

    private TransferVo listByUser(RoleUserListParam param){
        TransferVo vo = new TransferVo();
        vo.setId(param.getId());

        List<SysRole> roles = sysRoleMapper.selectByExample(new SysRoleExample());
        List<TransferVo.TransferData> list = new ArrayList<>();
        for (SysRole role:roles){
            TransferVo.TransferData data = new TransferVo.TransferData();
            data.setKey(role.getId());
            data.setLabel(role.getName());
            data.setDisabled(!SysConstant.RoleStatus.NORMAL.equals(role.getStatus()));
            list.add(data);
        }
        vo.setData(list);

        SysRoleUserExample example = new SysRoleUserExample();
        example.createCriteria().andUserIdEqualTo(param.getId());
        List<SysRoleUser> roleUsers = sysRoleUserMapper.selectByExample(example);
        vo.setValue(roleUsers.stream().map(SysRoleUser::getRoleId).collect(Collectors.toSet()));
        return vo;
    }

    private TransferVo listByRole(RoleUserListParam param){
        TransferVo vo = new TransferVo();
        vo.setId(param.getId());

        List<SysUser> users = sysUserMapper.selectByExample(new SysUserExample());
        List<TransferVo.TransferData> list = new ArrayList<>();
        for (SysUser user:users){
            TransferVo.TransferData data = new TransferVo.TransferData();
            data.setKey(user.getId());
            data.setLabel(user.getUsername());
            data.setDisabled(!SysConstant.UserStatus.NORMAL.equals(user.getStatus()));
            list.add(data);
        }
        vo.setData(list);

        SysRoleUserExample example = new SysRoleUserExample();
        example.createCriteria().andRoleIdEqualTo(param.getId());
        List<SysRoleUser> roleUsers = sysRoleUserMapper.selectByExample(example);
        vo.setValue(roleUsers.stream().map(SysRoleUser::getUserId).collect(Collectors.toSet()));

        return vo;
    }

    @Override
    public void publish(RoleUserFormParam param) {
        Event<RoleUserFormParam> event = new Event<>();
        event.setTopic(messageProcessor.getTopic());
        event.setModule(EventConstants.Module.WEB_SYS);
        event.setType(EventConstants.Type.ROLE_USER);
        event.setData(param);
        messageProcessor.publish(event);
    }
}
