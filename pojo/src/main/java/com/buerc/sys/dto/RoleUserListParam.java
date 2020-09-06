package com.buerc.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RoleUserListParam {
    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "1:通过角色id查 2:通过用户id查")
    @NotNull(message = "类型不能为空")
    @Range(min = 1,max = 2,message = "类型只能 1-2之间")
    private Byte type;
}
