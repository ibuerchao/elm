package com.buerc.permission.param;

import com.buerc.common.constants.SysConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

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
    private String parentId = SysConstant.Sys.DEFAULT_DEPT_PARENT_ID;

    @ApiModelProperty(value = "部门状态 1正常 0禁用")
    @NotNull(message = "状态不能为空")
    @Range(min = 0,max = 1,message = "部门状态只能 0-1之间")
    private Byte status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
