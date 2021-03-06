package com.buerc.permission.service;

import com.buerc.common.web.Result;
import com.buerc.permission.model.SysUser;
import com.buerc.permission.model.SysUserExample;
import com.buerc.sys.bo.UserInfo;
import com.buerc.sys.dto.*;
import com.buerc.sys.vo.UserVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysUserService {
    /**
     * 用户注册
     * @param signUp 用户注册信息
     * @return token
     */
    void signUp(SignUpParam signUp);

    /**
     * 注册成功后，激活邮箱
     * @param payload token
     */
    Map<String,Object> validatePayload(String payload);

    /**
     * 用户登录
     */
    String login(LoginParam user);

    Boolean logOut();

    List<SysUser> selectByExample(SysUserExample example);

    SysUser selectByPrimaryKey(String id);

    Set<String> getRolesByUserId(String id);

    Set<String> getPermissionsByUserId(String id);

    /**
     * 获取邮箱验证码
     */
    void getCode(String email);

    /**
     * 重置密码
     */
    void resetPassword(ResetPasswordParam resetPassword);

    UserInfo infoByToken(String token);

    UserInfo infoByUserId(String userId);

    /**
     * 新增用户
     */
    UserVo add(UserFormParam userFormParam);

    /**
     * 删除用户
     */
    void delete(String id);

    /**
     * 编辑用户
     */
    UserVo edit(UserFormParam param);

    /**
     * 用户详情
     */
    UserVo detail(String id);

    /**
     * 更改状态
     */
    boolean updateStatus(UpdateStatusParam param);

    Result<List<UserVo>> list(UserListParam param);

    void publish(String userId);
}
