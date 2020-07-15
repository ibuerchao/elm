package com.buerc.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RoleFormParam {
    @ApiModelProperty(value = "角色主键(编辑时必传)")
    private String id;

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 1,max = 20,message = "角色名称长度在 1 到 20 个字符")
    private String name;

    @ApiModelProperty(value = "角色编码")
    @NotBlank(message = "角色编码不能为空")
    @Size(min = 1,max = 50,message = "角色编码长度在 1 到 50 个字符")
    private String code;

    @ApiModelProperty(value = "角色状态")
    @NotNull(message = "角色状态不能为空")
    @Range(min = 0,max = 1,message = "角色状态")
    private Byte status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
