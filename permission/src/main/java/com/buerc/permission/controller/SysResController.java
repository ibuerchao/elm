package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.utils.BeanValidator;
import com.buerc.common.web.Result;
import com.buerc.permission.service.SysResService;
import com.buerc.sys.dto.ResFormParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/res")
//@OperateLog(value = "资源管理",module = 3)
public class SysResController {
    @Resource
    private SysResService sysResService;

    /**
     * 新增资源权限
     */
    @ApiOperation(value = "新增资源")
    @PostMapping("/add")
    public Result add(@RequestBody ResFormParam param){
        BeanValidator.validator(param);
        return Result.success(sysResService.add(param));
    }
}
