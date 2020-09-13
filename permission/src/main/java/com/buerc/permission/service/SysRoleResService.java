package com.buerc.permission.service;

import com.buerc.sys.dto.RoleResFormParam;
import com.buerc.sys.dto.RoleResListParam;

import java.util.Set;

public interface SysRoleResService {
    void save(RoleResFormParam param);

    Set<String> list(RoleResListParam param);
}
