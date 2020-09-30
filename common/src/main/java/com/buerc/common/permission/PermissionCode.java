package com.buerc.common.permission;

import com.buerc.common.permission.sys.*;

//权限编码格式  应用模块:菜单模块:功能模块:操作模块
public interface PermissionCode {
    interface Sys extends Dept, Res, ResModule, Role, RoleRes, RoleUser, User {
    }
}