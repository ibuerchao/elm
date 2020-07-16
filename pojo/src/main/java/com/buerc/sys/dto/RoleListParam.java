package com.buerc.sys.dto;

import com.buerc.common.constants.SysConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RoleListParam {
    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色编码")
    private String code;

    @ApiModelProperty(value = "角色状态")
    private List<Byte> status;

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
