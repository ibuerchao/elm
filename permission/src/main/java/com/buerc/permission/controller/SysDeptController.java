package com.buerc.permission.controller;

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
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    @ApiOperation(value = "新增部门")
    @PostMapping("/add")
    public Result add(@RequestBody DeptFormParam dept){
        BeanValidator.validator(dept);
        return Result.success(sysDeptService.add(dept));
    }

    @ApiOperation(value = "编辑部门")
    @PostMapping("/edit")
    public Result edit(@RequestBody DeptFormParam dept){
        BeanValidator.validator(dept);
        return Result.success(sysDeptService.edit(dept));
    }

    @ApiOperation(value = "部门列表")
    @PostMapping("/list")
    public Result list(@RequestBody DeptListParam param){
        BeanValidator.validator(param);
        return sysDeptService.list(param);
    }

    @ApiOperation(value = "部门详情")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id){
        return Result.success(sysDeptService.detail(id));
    }

    @ApiOperation(value = "上移部门")
    @GetMapping("/up/{id}")
    public Result up(@PathVariable String id){
        sysDeptService.up(id);
        return Result.success();
    }

    @ApiOperation(value = "下移部门")
    @GetMapping("/down/{id}")
    public Result down(@PathVariable String id){
        sysDeptService.down(id);
        return Result.success();
    }
}
