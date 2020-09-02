package com.buerc.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoleResFormParam {
    @ApiModelProperty(value = "角色ID")
    @NotBlank(message = "角色ID不能为空")
    private String roleId;

    @ApiModelProperty(value = "1:组织 2:模块 3:资源")
    @NotNull(message = "类型不能为空")
    @Range(min = 1,max = 3,message = "类型只能 1-3之间")
    private Byte targetType;

    @ApiModelProperty(value = "targetIds")
    @NotEmpty(message = "targetIds不能为空")
    private List<String> targetIds;
}
