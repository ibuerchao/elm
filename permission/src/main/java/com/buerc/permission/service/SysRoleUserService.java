package com.buerc.permission.service;

import com.buerc.sys.dto.RoleUserFormParam;
import com.buerc.sys.dto.RoleUserListParam;
import com.buerc.sys.vo.TransferVo;

public interface SysRoleUserService {
    void save(RoleUserFormParam param);

    TransferVo list(RoleUserListParam param);

    void publish(RoleUserFormParam param);
}
