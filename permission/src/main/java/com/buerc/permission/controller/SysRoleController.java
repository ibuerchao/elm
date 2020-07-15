package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.utils.BeanValidator;
import com.buerc.common.web.Result;
import com.buerc.permission.service.SysRoleService;
import com.buerc.sys.dto.RoleFormParam;
import com.buerc.sys.dto.RoleListParam;
import com.buerc.sys.dto.UpdateStatusParam;
import com.buerc.sys.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/role")
@OperateLog(value = "角色管理",module = 5)
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;

    @ApiOperation(value = "新增角色")
    @PostMapping("/add")
    @OperateLog(value = "新增角色",type = 1)
    public Result add(@RequestBody RoleFormParam param){
        BeanValidator.validator(param);
        return Result.success(sysRoleService.add(param));
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/delete/{id}")
    @OperateLog(value = "删除角色",type = 2)
    public Result add(@PathVariable("id") String id){
        sysRoleService.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "编辑角色")
    @PostMapping("/edit")
    @OperateLog(value = "编辑角色",type = 3)
    public Result edit(@RequestBody RoleFormParam param){
        BeanValidator.validator(param);
        return Result.success(sysRoleService.edit(param));
    }

    @ApiOperation(value = "角色详情")
    @GetMapping("/detail/{id}")
    @OperateLog(value = "角色详情",type = 4)
    public Result detail(@PathVariable String id){
        return Result.success(sysRoleService.detail(id));
    }

    @ApiOperation(value = "更新状态")
    @PostMapping("/update/status")
    @OperateLog(value = "更新状态",type = 3)
    public Result updateStatus(@RequestBody UpdateStatusParam param){
        BeanValidator.validator(param);
        boolean updateStatus = sysRoleService.updateStatus(param);
        if (updateStatus){
            return Result.success();
        }else {
            return Result.error();
        }
    }

    @ApiOperation(value = "角色列表")
    @PostMapping("/list")
    @OperateLog(value = "角色列表",type = 5)
    public Result<List<UserVo>> list(@RequestBody RoleListParam param){
        return sysRoleService.list(param);
    }
}
