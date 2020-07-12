package com.buerc.sys.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserVo {
    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "部门名称")
    private String deptname;

    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "用户所在部门的id")
    private String deptId;

    @ApiModelProperty(value = "状态，1：正常，0：冻结状态，2：删除")
    private Byte status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "用户id")
    private String operateId;

    @ApiModelProperty(value = "操作者")
    private String operateName;

    @ApiModelProperty(value = "最后一次更新时间")
    private Date operateTime;

    @ApiModelProperty(value = "最后一次更新者的ip地址")
    private String operateIp;

}
