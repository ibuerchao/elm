package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.utils.BeanValidator;
import com.buerc.common.web.Result;
import com.buerc.permission.model.SysPermission;
import com.buerc.permission.service.SysResService;
import com.buerc.sys.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/res")
@OperateLog(value = "资源管理",module = 4)
public class SysResController {
    @Resource
    private SysResService sysResService;

    @ApiOperation(value = "新增资源")
    @PostMapping("/add")
    @OperateLog(value = "新增资源",type = 1)
    public Result add(@RequestBody ResFormParam param){
        BeanValidator.validator(param);
        return Result.success(sysResService.add(param));
    }

    @ApiOperation(value = "删除资源")
    @DeleteMapping("/delete/{id}")
    @OperateLog(value = "删除资源",type = 2)
    public Result add(@PathVariable("id") String id){
        sysResService.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "编辑资源")
    @PostMapping("/edit")
    @OperateLog(value = "编辑资源",type = 3)
    public Result edit(@RequestBody ResFormParam param){
        BeanValidator.validator(param);
        return Result.success(sysResService.edit(param));
    }

    @ApiOperation(value = "资源详情")
    @GetMapping("/detail/{id}")
    @OperateLog(value = "资源详情",type = 4)
    public Result detail(@PathVariable String id){
        return Result.success(sysResService.detail(id));
    }

    @ApiOperation(value = "更新状态")
    @PostMapping("/update/status")
    @OperateLog(value = "更新状态",type = 3)
    public Result updateStatus(@RequestBody UpdateStatusParam param){
        BeanValidator.validator(param);
        boolean updateStatus = sysResService.updateStatus(param);
        if (updateStatus){
            return Result.success();
        }else {
            return Result.error();
        }
    }

    @ApiOperation(value = "资源列表")
    @PostMapping("/list")
    @OperateLog(value = "资源列表",type = 5)
    public Result<List<SysPermission>> list(@RequestBody ResListParam param){
        return sysResService.list(param);
    }
}
