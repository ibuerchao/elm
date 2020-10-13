package com.buerc.security.shiro;

import com.buerc.common.utils.JwtTokenUtil;
import com.buerc.security.holder.SecurityContextHolder;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

@Component
public class AuthRealm extends AuthorizingRealm {
    private static final String SUPER_ADMIN = "SUPER_ADMIN";

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
        return new SimpleAuthenticationInfo(userId, token, this.getName());
    }

    @Override
    protected boolean isPermitted(Permission permission, AuthorizationInfo info) {
        return info.getRoles().contains(SUPER_ADMIN) || super.isPermitted(permission, info);
    }

    @Override
    protected boolean hasRole(String roleIdentifier, AuthorizationInfo info) {
        return info.getRoles().contains(SUPER_ADMIN) || super.hasRole(roleIdentifier, info);
    }

    private Set<String> roles(String userId) {
        return SecurityContextHolder.get(userId).getRoles();
    }

    private Collection<String> permissions(String userId) {
        return SecurityContextHolder.get(userId).getPermissions();
    }
}
