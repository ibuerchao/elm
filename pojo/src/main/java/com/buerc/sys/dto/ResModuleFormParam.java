package com.buerc.sys.dto;

import com.buerc.common.constants.SysConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 新增和编辑权限 实体类
 */
@Data
public class ResModuleFormParam {
    @ApiModelProperty(value = "模块主键(编辑时必传)")
    private String id;

    @ApiModelProperty(value = "模块名称")
    @NotBlank(message = "模块名称不能为空")
    @Size(min = 1,max = 20,message = "模块名称长度在 1 到 20 个字符")
    private String name;

    @ApiModelProperty(value = "权限所在的权限模块id")
    private String parentId = SysConstant.Sys.DEFAULT_RES_MODULE_ID;

    @ApiModelProperty(value = "状态，1：正常，0：冻结")
    @NotNull(message = "状态不能为空")
    @Range(min = 0,max = 1,message = "状态可选值 1：正常，0：冻结")
    private Byte status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
