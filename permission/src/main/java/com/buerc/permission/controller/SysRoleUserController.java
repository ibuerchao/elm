package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.annotation.ParamValid;
import com.buerc.common.permission.PermissionCode;
import com.buerc.common.web.Result;
import com.buerc.permission.service.SysRoleUserService;
import com.buerc.sys.dto.RoleUserFormParam;
import com.buerc.sys.dto.RoleUserListParam;
import com.buerc.sys.vo.TransferVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/role_user")
@OperateLog(value = "角色用户管理",module = 6)
@ParamValid
public class SysRoleUserController {

    @Resource
    private SysRoleUserService sysRoleUserService;

    @RequiresPermissions(PermissionCode.Sys.ROLE_USER_SAVE)
    @ApiOperation(value = "保存角色用户")
    @PostMapping("/save")
    @OperateLog(value = "保存角色用户",type = 1)
    public Result save(@RequestBody RoleUserFormParam param){
        sysRoleUserService.save(param);
        sysRoleUserService.publish(param);
        return Result.success();
    }

    @RequiresPermissions(PermissionCode.Sys.ROLE_USER_LIST)
    @ApiOperation(value = "角色用户列表")
    @PostMapping("/list")
    @OperateLog(value = "角色用户列表",type = 5)
    public Result<TransferVo> list(@RequestBody RoleUserListParam param){
        return Result.success(sysRoleUserService.list(param));
    }
}
