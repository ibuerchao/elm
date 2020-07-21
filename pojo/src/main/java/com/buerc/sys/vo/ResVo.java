package com.buerc.sys.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ResVo {
    @ApiModelProperty(value = "资源ID")
    private String id;

    @ApiModelProperty(value = "资源编码")
    private String code;

    @ApiModelProperty(value = "资源名称")
    private String name;

    @ApiModelProperty(value = "资源所在的模块ID")
    private String moduleId;

    @ApiModelProperty(value = "资源所在的模块名称")
    private String moduleName;

    @ApiModelProperty(value = "请求的url, 可以填正则表达式")
    private String url;

    @ApiModelProperty(value = "类型，1：菜单，2：按钮，3：其他")
    private Byte type;

    @ApiModelProperty(value = "状态，1：正常，0：冻结")
    private Byte status;

    @ApiModelProperty(value = "资源在当前模块下的顺序，由小到大")
    private Integer seq;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "用户id")
    private String operateId;

    @ApiModelProperty(value = "操作者")
    private String operateName;

    @ApiModelProperty(value = "最后一次更新时间")
    private Date operateTime;

    @ApiModelProperty(value = "最后一个更新者的ip地址")
    private String operateIp;

}
