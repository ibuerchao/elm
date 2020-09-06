package com.buerc.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class RoleUserFormParam {
    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "目标id")
    private Set<String> targetIds;

    @ApiModelProperty(value = "1:为角色id指定用户 2:为用户id指定角色")
    @NotNull(message = "类型不能为空")
    @Range(min = 1,max = 2,message = "类型只能 1-2之间")
    private Byte type;
}
