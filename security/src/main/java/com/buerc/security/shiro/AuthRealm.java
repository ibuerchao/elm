package com.buerc.security.shiro;

import com.buerc.common.utils.JwtTokenUtil;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class AuthRealm extends AuthorizingRealm {
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userId = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles(userId));
        simpleAuthorizationInfo.addStringPermissions(permissions(userId));
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getPrincipal();
        String userId = jwtTokenUtil.getUserId(token);
        return new SimpleAuthenticationInfo(userId,token,this.getName());
    }

    //todo 调用permission服务接口获取角色
    private Set<String> roles(String userId){
        System.out.println(userId);
        return new HashSet<>();
    }

    //todo 调用permission服务接口获取权限
    private Collection<String> permissions(String userId){
        System.out.println(userId);
        return new HashSet<>();
    }
}
