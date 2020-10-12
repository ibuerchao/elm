package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.annotation.ParamValid;
import com.buerc.common.permission.PermissionCode;
import com.buerc.common.web.Result;
import com.buerc.permission.service.SysUserService;
import com.buerc.sys.dto.UpdateStatusParam;
import com.buerc.sys.dto.UserFormParam;
import com.buerc.sys.dto.UserListParam;
import com.buerc.sys.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
@OperateLog(value = "用户管理",module = 2)
@ParamValid
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    @RequiresPermissions(PermissionCode.Sys.USER_ADD)
    @ApiOperation(value = "新增用户")
    @PostMapping("/add")
    @OperateLog(value = "新增用户",type = 1)
    public Result<UserVo> add(@RequestBody UserFormParam userFormParam){
        return Result.success(sysUserService.add(userFormParam));
    }

    @RequiresPermissions(PermissionCode.Sys.USER_DELETE)
    @ApiOperation(value = "删除用户")
    @DeleteMapping("/delete/{id}")
    @OperateLog(value = "删除用户",type = 2)
    public Result add(@PathVariable("id") String id){
        sysUserService.delete(id);
        return Result.success();
    }

    @RequiresPermissions(PermissionCode.Sys.USER_EDIT)
    @ApiOperation(value = "编辑用户")
    @PostMapping("/edit")
    @OperateLog(value = "编辑用户",type = 3)
    public Result edit(@RequestBody UserFormParam param){
        return Result.success(sysUserService.edit(param));
    }

    @RequiresPermissions(PermissionCode.Sys.USER_DETAIL)
    @ApiOperation(value = "用户详情")
    @GetMapping("/detail/{id}")
    @OperateLog(value = "用户详情",type = 4)
    public Result detail(@PathVariable String id){
        return Result.success(sysUserService.detail(id));
    }

    @RequiresPermissions(PermissionCode.Sys.USER_UPDATE_STATUS)
    @ApiOperation(value = "更新状态")
    @PostMapping("/update/status")
    @OperateLog(value = "更新状态",type = 3)
    public Result updateStatus(@RequestBody UpdateStatusParam param){
        boolean updateStatus = sysUserService.updateStatus(param);
        if (updateStatus){
            return Result.success();
        }else {
            return Result.error();
        }
    }

    @RequiresPermissions(PermissionCode.Sys.USER_LIST)
    @ApiOperation(value = "用户列表")
    @PostMapping("/list")
    @OperateLog(value = "用户列表",type = 5)
    public Result<List<UserVo>> list(@RequestBody UserListParam param){
        return sysUserService.list(param);
    }
}
