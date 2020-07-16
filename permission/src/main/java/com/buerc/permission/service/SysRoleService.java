package com.buerc.permission.service;

import com.buerc.common.web.Result;
import com.buerc.permission.model.SysRole;
import com.buerc.sys.dto.RoleFormParam;
import com.buerc.sys.dto.RoleListParam;
import com.buerc.sys.dto.UpdateStatusParam;

import java.util.List;

public interface SysRoleService {
    SysRole add(RoleFormParam param);

    void delete(String id);

    SysRole edit(RoleFormParam param);

    SysRole detail(String id);

    boolean updateStatus(UpdateStatusParam param);

    Result<List<SysRole>> list(RoleListParam param);
}
