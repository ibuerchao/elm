package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.utils.BeanValidator;
import com.buerc.common.web.Result;
import com.buerc.permission.service.SysUserService;
import com.buerc.sys.dto.UpdateStatusParam;
import com.buerc.sys.dto.UserFormParam;
import com.buerc.sys.dto.UserListParam;
import com.buerc.sys.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
@OperateLog(value = "用户管理",module = 2)
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

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

    @ApiOperation(value = "用户详情")
    @GetMapping("/detail/{id}")
    @OperateLog(value = "用户详情",type = 4)
    public Result detail(@PathVariable String id){
        return Result.success(sysUserService.detail(id));
    }

    @ApiOperation(value = "更新状态")
    @PostMapping("/update/status")
    @OperateLog(value = "更新状态",type = 3)
    public Result updateStatus(@RequestBody UpdateStatusParam param){
        BeanValidator.validator(param);
        boolean updateStatus = sysUserService.updateStatus(param);
        if (updateStatus){
            return Result.success();
        }else {
            return Result.error();
        }
    }

//    @RequiresRoles(SysConstant.UserCode.QUERY_USER_LIST)
    @ApiOperation(value = "用户列表")
    @PostMapping("/list")
    @OperateLog(value = "用户列表",type = 5)
    public Result<List<UserVo>> list(@RequestBody UserListParam param){
        return sysUserService.list(param);
    }
}
