package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.utils.BeanValidator;
import com.buerc.common.web.Result;
import com.buerc.permission.model.SysPermissionModule;
import com.buerc.permission.service.SysResModuleService;
import com.buerc.sys.dto.ResListParam;
import com.buerc.sys.dto.ResModuleFormParam;
import com.buerc.sys.dto.ResModuleListParam;
import com.buerc.sys.dto.UpdateStatusParam;
import com.buerc.sys.vo.ResModuleVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/res/module")
@OperateLog(value = "模块管理",module = 4)
public class SysResModuleController {
    @Resource
    private SysResModuleService sysResModuleService;

    @ApiOperation(value = "新增模块")
    @PostMapping("/add")
    @OperateLog(value = "新增模块",type = 1)
    public Result add(@RequestBody ResModuleFormParam param){
        BeanValidator.validator(param);
        return Result.success(sysResModuleService.add(param));
    }

    @ApiOperation(value = "删除模块")
    @DeleteMapping("/delete/{id}")
    @OperateLog(value = "删除模块",type = 2)
    public Result add(@PathVariable("id") String id){
        sysResModuleService.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "编辑模块")
    @PostMapping("/edit")
    @OperateLog(value = "编辑模块",type = 3)
    public Result edit(@RequestBody ResModuleFormParam param){
        BeanValidator.validator(param);
        return Result.success(sysResModuleService.edit(param));
    }

    @ApiOperation(value = "模块详情")
    @GetMapping("/detail/{id}")
    @OperateLog(value = "模块详情",type = 4)
    public Result detail(@PathVariable String id){
        return Result.success(sysResModuleService.detail(id));
    }

    @ApiOperation(value = "更新状态")
    @PostMapping("/update/status")
    @OperateLog(value = "更新状态",type = 3)
    public Result updateStatus(@RequestBody UpdateStatusParam param){
        BeanValidator.validator(param);
        boolean updateStatus = sysResModuleService.updateStatus(param);
        if (updateStatus){
            return Result.success();
        }else {
            return Result.error();
        }
    }

    @ApiOperation(value = "模块列表")
    @PostMapping("/list")
    @OperateLog(value = "模块列表",type = 5)
    public Result<List<ResModuleVo>> list(@RequestBody ResModuleListParam param){
        return sysResModuleService.list(param);
    }

    @ApiOperation(value = "模块树")
    @GetMapping("/superior")
    @OperateLog(value = "模块树",type = 8)
    public Result superior(@RequestParam("id") String id,
                           @RequestParam(value = "status",required = false) String status){
        return Result.success(sysResModuleService.superior(id,status));
    }

    @ApiOperation(value = "上移模块")
    @GetMapping("/up/{id}")
    @OperateLog(value = "上移模块",type = 6)
    public Result up(@PathVariable String id){
        sysResModuleService.up(id);
        return Result.success();
    }

    @ApiOperation(value = "下移模块")
    @GetMapping("/down/{id}")
    @OperateLog(value = "下移模块",type = 7)
    public Result down(@PathVariable String id){
        sysResModuleService.down(id);
        return Result.success();
    }
}
