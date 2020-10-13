package com.buerc.permission.service;

import com.buerc.common.web.Result;
import com.buerc.permission.model.SysRole;
import com.buerc.sys.dto.RoleFormParam;
import com.buerc.sys.dto.RoleListParam;
import com.buerc.sys.dto.UpdateStatusParam;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;

public interface SysRoleService {
    SysRole add(RoleFormParam param);

    Set<String> delete(String id);

    Pair<SysRole,Boolean> edit(RoleFormParam param);

    SysRole detail(String id);

    boolean updateStatus(UpdateStatusParam param);

    Result<List<SysRole>> list(RoleListParam param);

    /**
     * 角色删除成功之后，发送redis广播事件刷新应用缓存
     * @param userIds 用户id
     */
    void publish(Set<String> userIds);

    /**
     * 角色状态发生变化之后，发送redis广播事件刷新应用缓存
     * @param roleId 角色id
     */
    void publish(String roleId);
}
