package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.constants.SysConstant;
import com.buerc.common.utils.BeanValidator;
import com.buerc.common.web.Result;
import com.buerc.permission.service.SysUserService;
import com.buerc.sys.dto.UserFormParam;
import com.buerc.sys.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
@OperateLog(value = "用户管理",module = 2)
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
    @OperateLog(value = "新增用户",type = 1)
    public Result<UserVo> add(@RequestBody UserFormParam userFormParam){
        BeanValidator.validator(userFormParam);
        return Result.success(sysUserService.add(userFormParam));
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/delete/{id}")
    @OperateLog(value = "删除用户",type = 2)
    public Result add(@PathVariable("id") String id){
        sysUserService.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "编辑用户")
    @PostMapping("/edit")
    @OperateLog(value = "编辑用户",type = 3)
    public Result edit(@RequestBody UserFormParam param){
        BeanValidator.validator(param);
        return Result.success(sysUserService.edit(param));
    }
}
