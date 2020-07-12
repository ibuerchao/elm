package com.buerc.sys.dto;

import com.buerc.common.constants.SysConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Data
public class UserListParam {
    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "用户状态 0冻结 1正常 2删除 3未激活 4锁定")
    @Min(value = 0, message = "用户状态 0冻结 1正常 2删除 3未激活 4锁定")
    @Max(value = 4, message = "用户状态 0冻结 1正常 2删除 3未激活 4锁定")
    private Byte status;

    @ApiModelProperty(value = "部门ID")
    private String deptId;

    @ApiModelProperty(value = "开始时间")
    private Date start;

    @ApiModelProperty(value = "结束时间")
    private Date end;

    @ApiModelProperty(value = "排序规则")
    private String order = "operate_time desc";

    @ApiModelProperty(value = "分页起始值")
    private Integer offset = SysConstant.Sys.OFFSET;

    @ApiModelProperty(value = "分页每页大小")
    private Integer limit = SysConstant.Sys.LIMIT;
}
