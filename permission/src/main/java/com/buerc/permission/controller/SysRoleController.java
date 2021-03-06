package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.annotation.ParamValid;
import com.buerc.common.permission.PermissionCode;
import com.buerc.common.web.Result;
import com.buerc.permission.model.SysRole;
import com.buerc.permission.service.SysRoleService;
import com.buerc.sys.dto.RoleFormParam;
import com.buerc.sys.dto.RoleListParam;
import com.buerc.sys.dto.UpdateStatusParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/role")
@OperateLog(value = "角色管理",module = 5)
@ParamValid
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;

    @RequiresPermissions(PermissionCode.Sys.ROLE_ADD)
    @ApiOperation(value = "新增角色")
    @PostMapping("/add")
    @OperateLog(value = "新增角色",type = 1)
    public Result add(@RequestBody RoleFormParam param){
        return Result.success(sysRoleService.add(param));
    }

    @RequiresPermissions(PermissionCode.Sys.ROLE_DELETE)
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/delete/{id}")
    @OperateLog(value = "删除角色",type = 2)
    public Result add(@PathVariable("id") String id){
        Set<String> userIds = sysRoleService.delete(id);
        sysRoleService.publish(userIds);
        return Result.success();
    }

    @RequiresPermissions(PermissionCode.Sys.ROLE_EDIT)
    @ApiOperation(value = "编辑角色")
    @PostMapping("/edit")
    @OperateLog(value = "编辑角色",type = 3)
    public Result edit(@RequestBody RoleFormParam param){
        Pair<SysRole, Boolean> pair = sysRoleService.edit(param);
        if (!pair.getRight()){
            sysRoleService.publish(param.getId());
        }
        return Result.success(pair.getLeft());
    }

    @RequiresPermissions(PermissionCode.Sys.ROLE_DETAIL)
    @ApiOperation(value = "角色详情")
    @GetMapping("/detail/{id}")
    @OperateLog(value = "角色详情",type = 4)
    public Result detail(@PathVariable String id){
        return Result.success(sysRoleService.detail(id));
    }

    @RequiresPermissions(PermissionCode.Sys.ROLE_UPDATE_STATUS)
    @ApiOperation(value = "更新状态")
    @PostMapping("/update/status")
    @OperateLog(value = "更新状态",type = 3)
    public Result updateStatus(@RequestBody UpdateStatusParam param){
        boolean updateStatus = sysRoleService.updateStatus(param);
        if (updateStatus){
            sysRoleService.publish(param.getId());
            return Result.success();
        }else {
            return Result.error();
        }
    }

    @RequiresPermissions(PermissionCode.Sys.ROLE_LIST)
    @ApiOperation(value = "角色列表")
    @PostMapping("/list")
    @OperateLog(value = "角色列表",type = 5)
    public Result<List<SysRole>> list(@RequestBody RoleListParam param){
        return sysRoleService.list(param);
    }
}
