package com.buerc.permission.shiro;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.JwtTokenUtil;
import com.buerc.permission.model.SysUser;
import com.buerc.permission.service.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AuthRealm extends AuthorizingRealm {
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private SysUserService sysUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userId = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(sysUserService.getRolesByUserId(userId));
        simpleAuthorizationInfo.addStringPermissions(sysUserService.getPermissionsByUserId(userId));
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getPrincipal();
        String userId = jwtTokenUtil.getUserId(token);
        return new SimpleAuthenticationInfo(userId,token,this.getName());
    }
}
