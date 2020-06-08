package com.buerc.permission.service;

import com.buerc.permission.model.SysUser;
import com.buerc.permission.model.SysUserExample;
import com.buerc.permission.param.SignUp;
import com.buerc.permission.param.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysUserService {
    /**
     * 用户注册
     * @param signUp 用户注册信息
     * @return token
     */
    String signUp(SignUp signUp);

    /**
     * 注册成功后，激活邮箱
     * @param payload token
     */
    Map<String,Object> validateEmail(String payload);

    /**
     * 用户登录
     */
    String login(User user);

    Boolean logOut();

    List<SysUser> selectByExample(SysUserExample example);

    SysUser selectByPrimaryKey(String id);

    Set<String> getRolesByUserId(String id);

    Set<String> getPermissionsByUserId(String id);
}
