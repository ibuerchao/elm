package com.buerc.permission.service.impl;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import com.buerc.permission.mapper.SysRoleMapper;
import com.buerc.permission.mapper.SysRoleUserMapper;
import com.buerc.permission.mapper.SysUserMapper;
import com.buerc.permission.model.*;
import com.buerc.permission.service.SysRoleUserService;
import com.buerc.sys.dto.RoleUserFormParam;
import com.buerc.sys.dto.RoleUserListParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SysRoleUserServiceImpl implements SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public void save(RoleUserFormParam param) {
        check(param);
        delete(param);
        insert(param);
    }

    private void check(RoleUserFormParam param){
        if (param.getType() == 1){
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
        SysRoleExample example = new SysRoleExample();
        example.createCriteria().andIdIn(new ArrayList<>(roleIds));
        List<SysRole> roles = sysRoleMapper.selectByExample(example);
        if (roles.size() != roleIds.size()) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.CONTAIN_NOT_EXIST_ROLE_MSG);
        }
    }

    private void checkUser(Set<String> userIds) {
        SysUserExample example = new SysUserExample();
        example.createCriteria().andIdIn(new ArrayList<>(userIds));
        List<SysUser> users = sysUserMapper.selectByExample(example);
        if (users.size() != userIds.size()) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.CONTAIN_NOT_EXIST_USER_MSG);
        }
    }

    private void delete(RoleUserFormParam param) {
        SysRoleUserExample example = new SysRoleUserExample();
        SysRoleUserExample.Criteria criteria = example.createCriteria();
        if (param.getType() == 1){
            criteria.andRoleIdEqualTo(param.getId());
        }else{
            criteria.andUserIdEqualTo(param.getId());
        }
        sysRoleUserMapper.deleteByExample(example);
    }

    private void insert(RoleUserFormParam param){
        List<SysRoleUser> list = new ArrayList<>();
        for (String id:param.getTargetIds()){
            SysRoleUser sysRoleUser = new SysRoleUser();
            if (param.getType() == 1){
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

    @Override
    public List<SysRoleUser> list(RoleUserListParam param) {
        SysRoleUserExample example = new SysRoleUserExample();
        SysRoleUserExample.Criteria criteria = example.createCriteria();
        if (param.getType() == 1){
            criteria.andRoleIdEqualTo(param.getId());
        }else {
            criteria.andUserIdEqualTo(param.getId());
        }
        return sysRoleUserMapper.selectByExample(example);
    }
}
