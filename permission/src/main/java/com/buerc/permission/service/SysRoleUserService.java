package com.buerc.permission.service;

import com.buerc.permission.model.SysRoleUser;
import com.buerc.sys.dto.RoleUserFormParam;
import com.buerc.sys.dto.RoleUserListParam;
import com.buerc.sys.vo.TransferVo;

import java.util.List;

public interface SysRoleUserService {
    void save(RoleUserFormParam param);

    TransferVo list(RoleUserListParam param);

    void publish(RoleUserFormParam param);

    List<SysRoleUser> getSysRoleUserByRoleId(String roleId);
}
