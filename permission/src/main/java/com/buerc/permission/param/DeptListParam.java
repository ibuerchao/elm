package com.buerc.permission.param;

import com.buerc.common.constants.SysConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Data
public class DeptListParam {
    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门状态 1正常 0禁用")
    @Min(value = 0, message = "部门状态 1正常 0禁用")
    @Max(value = 1, message = "部门状态 1正常 0禁用")
    private Byte status;

    @ApiModelProperty(value = "操作人名称")
    private String operateName;

    @ApiModelProperty(value = "父级ID")
    private String parentId;

    @ApiModelProperty(value = "开始时间")
    private Date start;

    @ApiModelProperty(value = "结束时间")
    private Date end;

    @ApiModelProperty(value = "分页起始值")
    private int offset = SysConstant.Sys.OFFSET;

    @ApiModelProperty(value = "分页每页大小")
    private int limit = SysConstant.Sys.LIMIT;
}
