package com.buerc.permission.controller;

import com.buerc.common.utils.BeanValidator;
import com.buerc.common.web.Result;
import com.buerc.permission.param.Dept;
import com.buerc.permission.service.SysDeptService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dept")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    @ApiOperation(value = "新增部门")
    @PostMapping("/add")
    public Result add(@RequestBody Dept dept){
        BeanValidator.validator(dept);
        return Result.success(sysDeptService.add(dept));
    }

    @ApiOperation(value = "编辑部门")
    @PostMapping("/edit")
    public Result edit(@RequestBody Dept dept){
        BeanValidator.validator(dept);
        return Result.success(sysDeptService.edit(dept));
    }
}
