package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.utils.BeanValidator;
import com.buerc.common.web.Result;
import com.buerc.permission.service.SysDeptService;
import com.buerc.sys.dto.DeptFormParam;
import com.buerc.sys.dto.DeptListParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dept")
@OperateLog(value = "部门管理",module = 1)
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    @ApiOperation(value = "新增部门")
    @PostMapping("/add")
    @OperateLog(value = "新增部门",type = 1)
    public Result add(@RequestBody DeptFormParam dept){
        BeanValidator.validator(dept);
        return Result.success(sysDeptService.add(dept));
    }

    @ApiOperation(value = "删除部门")
    @DeleteMapping("/delete/{id}")
    @OperateLog(value = "删除部门",type = 2)
    public Result delete(@PathVariable String id){
        sysDeptService.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "编辑部门")
    @PostMapping("/edit")
    @OperateLog(value = "编辑部门",type = 3)
    public Result edit(@RequestBody DeptFormParam dept){
        BeanValidator.validator(dept);
        return Result.success(sysDeptService.edit(dept));
    }

    @ApiOperation(value = "部门列表")
    @PostMapping("/list")
    @OperateLog(value = "部门列表",type = 5)
    public Result list(@RequestBody DeptListParam param){
        BeanValidator.validator(param);
        return sysDeptService.list(param);
    }

    @ApiOperation(value = "部门详情")
    @GetMapping("/detail/{id}")
    @OperateLog(value = "部门详情",type = 4)
    public Result detail(@PathVariable String id){
        return Result.success(sysDeptService.detail(id));
    }

    @ApiOperation(value = "上移部门")
    @GetMapping("/up/{id}")
    @OperateLog(value = "上移部门",type = 3)
    public Result up(@PathVariable String id){
        sysDeptService.up(id);
        return Result.success();
    }

    @ApiOperation(value = "下移部门")
    @GetMapping("/down/{id}")
    @OperateLog(value = "下移部门",type = 3)
    public Result down(@PathVariable String id){
        sysDeptService.down(id);
        return Result.success();
    }
}
