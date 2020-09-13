package com.buerc.permission.service;

import com.buerc.common.web.Result;
import com.buerc.sys.dto.RoleResFormParam;
import com.buerc.sys.dto.RoleResListParam;

public interface SysRoleResService {
    void save(RoleResFormParam param);

    Result list(RoleResListParam param);
}
