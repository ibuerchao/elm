package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.utils.BeanValidator;
import com.buerc.common.web.Result;
import com.buerc.permission.model.SysRoleUser;
import com.buerc.permission.service.SysRoleUserService;
import com.buerc.sys.dto.RoleUserFormParam;
import com.buerc.sys.dto.RoleUserListParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/role_user")
@OperateLog(value = "角色用户管理",module = 6)
public class SysRoleUserController {

    @Resource
    private SysRoleUserService sysRoleUserService;

    @ApiOperation(value = "保存角色用户")
    @PostMapping("/save")
    @OperateLog(value = "保存角色用户",type = 1)
    public Result save(@RequestBody RoleUserFormParam param){
        BeanValidator.validator(param);
        sysRoleUserService.save(param);
        return Result.success();
    }

    @ApiOperation(value = "角色用户列表")
    @PostMapping("/list")
    @OperateLog(value = "角色用户列表",type = 5)
    public Result<List<SysRoleUser>> list(@RequestBody RoleUserListParam param){
        BeanValidator.validator(param);
        return Result.success(sysRoleUserService.list(param));
    }
}
