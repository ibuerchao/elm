package com.buerc.permission.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 新增部门实体类
 */
@Data
public class Dept {
    @ApiModelProperty(value = "部门主键(编辑时必传)")
    private String id;

    @ApiModelProperty(value = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 1,max = 20,message = "部门名称长度在 1 到 20 个字符")
    private String name;

    @ApiModelProperty(value = "上级部门ID")
    private String parentId = "root";

    @ApiModelProperty(value = "部门状态 1正常 0禁用")
    @NotNull(message = "状态不能为空")
    @Min(value = 0,message = "状态值最小为0")
    @Max(value = 1,message = "状态值最大为1")
    private Byte status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
