package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.annotation.ParamValid;
import com.buerc.common.permission.PermissionCode;
import com.buerc.common.web.Result;
import com.buerc.permission.service.SysRoleResService;
import com.buerc.sys.dto.RoleResFormParam;
import com.buerc.sys.dto.RoleResListParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/role_res")
@OperateLog(value = "角色资源管理",module = 7)
@ParamValid
public class SysRoleResController {

    @Resource
    private SysRoleResService sysRoleResService;

    @RequiresPermissions(PermissionCode.Sys.ROLE_RES_SAVE)
    @ApiOperation(value = "保存角色资源")
    @PostMapping("/save")
    @OperateLog(value = "保存角色资源",type = 1)
    public Result save(@RequestBody RoleResFormParam param){
        sysRoleResService.save(param);
        sysRoleResService.publish(param);
        return Result.success();
    }

    @RequiresPermissions(PermissionCode.Sys.ROLE_RES_LIST)
    @ApiOperation(value = "角色资源列表")
    @PostMapping("/list")
    @OperateLog(value = "角色资源列表",type = 5)
    public Result list(@RequestBody RoleResListParam param){
        return sysRoleResService.list(param);
    }
}
