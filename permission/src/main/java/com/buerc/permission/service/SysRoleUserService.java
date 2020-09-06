package com.buerc.permission.service;

import com.buerc.permission.model.SysRoleUser;
import com.buerc.sys.dto.RoleUserFormParam;
import com.buerc.sys.dto.RoleUserListParam;

import java.util.List;

public interface SysRoleUserService {
    void save(RoleUserFormParam param);

    List<SysRoleUser> list(RoleUserListParam param);
}
