package com.buerc.permission.controller;

import com.buerc.common.constants.SysConstant;
import com.buerc.common.vo.permission.UserInfo;
import com.buerc.common.web.Result;
import com.buerc.permission.model.SysUser;
import com.buerc.permission.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
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
    @PostMapping("/list")
    public Result<List<SysUser>> list(){
        List<SysUser> sysUsers = sysUserService.selectByExample(null);
        return Result.success(sysUsers);
    }

    @ApiImplicitParams(@ApiImplicitParam(name="token",value = "用户token",dataTypeClass = String.class))
    @ApiOperation(value = "通过token查询用户信息、角色、权限")
    @GetMapping("/info")
    public Result<UserInfo> info(@RequestParam("token") String token){
        return Result.success(sysUserService.info(token));
    }
}
