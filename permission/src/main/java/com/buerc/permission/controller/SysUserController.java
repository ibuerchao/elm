package com.buerc.permission.controller;

import com.buerc.common.constants.SysConstant;
import com.buerc.common.utils.BeanValidator;
import com.buerc.common.web.Result;
import com.buerc.permission.service.SysUserService;
import com.buerc.sys.dto.UserFormParam;
import com.buerc.sys.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    @RequiresRoles(SysConstant.UserCode.QUERY_USER_LIST)
    @ApiOperation(value = "查询用户列表")
    @PostMapping("/list")
    public Result<List<UserVo>> list(){
        return Result.success(null);
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/add")
    public Result<UserVo> add(@RequestBody UserFormParam userFormParam){
        BeanValidator.validator(userFormParam);
        return Result.success(sysUserService.add(userFormParam));
    }
}
