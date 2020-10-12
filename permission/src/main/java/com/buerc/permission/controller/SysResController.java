package com.buerc.permission.controller;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.annotation.ParamValid;
import com.buerc.common.permission.PermissionCode;
import com.buerc.common.web.Result;
import com.buerc.permission.service.SysResService;
import com.buerc.sys.dto.ResFormParam;
import com.buerc.sys.dto.ResListParam;
import com.buerc.sys.dto.UpdateStatusParam;
import com.buerc.sys.vo.ResVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/res")
@OperateLog(value = "资源管理",module = 4)
@ParamValid
public class SysResController {
    @Resource
    private SysResService sysResService;

    @RequiresPermissions(PermissionCode.Sys.RES_ADD)
    @ApiOperation(value = "新增资源")
    @PostMapping("/add")
    @OperateLog(value = "新增资源",type = 1)
    public Result add(@RequestBody ResFormParam param){
        return Result.success(sysResService.add(param));
    }

    @RequiresPermissions(PermissionCode.Sys.RES_DELETE)
    @ApiOperation(value = "删除资源")
    @DeleteMapping("/delete/{id}")
    @OperateLog(value = "删除资源",type = 2)
    public Result add(@PathVariable("id") String id){
        sysResService.delete(id);
        return Result.success();
    }

    @RequiresPermissions(PermissionCode.Sys.RES_EDIT)
    @ApiOperation(value = "编辑资源")
    @PostMapping("/edit")
    @OperateLog(value = "编辑资源",type = 3)
    public Result edit(@RequestBody ResFormParam param){
        return Result.success(sysResService.edit(param));
    }

    @RequiresPermissions(PermissionCode.Sys.RES_DETAIL)
    @ApiOperation(value = "资源详情")
    @GetMapping("/detail/{id}")
    @OperateLog(value = "资源详情",type = 4)
    public Result detail(@PathVariable String id){
        return Result.success(sysResService.detail(id));
    }

    @RequiresPermissions(PermissionCode.Sys.RES_UPDATE_STATUS)
    @ApiOperation(value = "更新状态")
    @PostMapping("/update/status")
    @OperateLog(value = "更新状态",type = 3)
    public Result updateStatus(@RequestBody UpdateStatusParam param){
        boolean updateStatus = sysResService.updateStatus(param);
        if (updateStatus){
            return Result.success();
        }else {
            return Result.error();
        }
    }

    @RequiresPermissions(PermissionCode.Sys.RES_LIST)
    @ApiOperation(value = "资源列表")
    @PostMapping("/list")
    @OperateLog(value = "资源列表",type = 5)
    public Result<List<ResVo>> list(@RequestBody ResListParam param){
        return sysResService.list(param);
    }
}
