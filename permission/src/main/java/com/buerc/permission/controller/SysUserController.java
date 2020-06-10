package com.buerc.permission.controller;

import com.buerc.common.constants.SysConstant;
import com.buerc.common.web.Result;
import com.buerc.permission.model.SysUser;
import com.buerc.permission.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sys")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    @ApiOperation(value = "用户登出")
    @PostMapping("/sign_out")
    public Result sign_out(){
        return Result.success(sysUserService.logOut());
    }

    @RequiresRoles(SysConstant.UserCode.QUERY_USER_LIST)
    @ApiOperation(value = "查询用户列表")
    @PostMapping("/user/list")
    public Result<List<SysUser>> list(){
        List<SysUser> sysUsers = sysUserService.selectByExample(null);
        return Result.success(sysUsers);
    }
}
