package com.buerc.permission.service;

import com.buerc.permission.model.SysRolePermission;
import com.buerc.sys.dto.RoleResFormParam;
import com.buerc.sys.dto.RoleResListParam;

import java.util.List;

public interface SysRoleResService {
    void save(RoleResFormParam param);

    List<SysRolePermission> list(RoleResListParam param);
}
