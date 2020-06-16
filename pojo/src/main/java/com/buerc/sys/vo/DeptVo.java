package com.buerc.sys.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DeptVo {
    @ApiModelProperty(value = "部门id")
    private String id;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "上级部门id")
    private String parentId;

    @ApiModelProperty(value = "1正常 0禁用")
    private Byte status;

    @ApiModelProperty(value = "部门在当前层级下的顺序，由小到大")
    private Integer seq;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "操作者")
    private String operateName;

    @ApiModelProperty(value = "最后一次操作时间")
    private Date operateTime;

    @ApiModelProperty(value = "最后一次更新操作者的ip地址")
    private String operateIp;

    @ApiModelProperty(value = "是否有子节点")
    private boolean hasChildren;
}
