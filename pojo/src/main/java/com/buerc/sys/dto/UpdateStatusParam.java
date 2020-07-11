package com.buerc.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 更新状态实体类
 */
@Data
public class UpdateStatusParam {
    @ApiModelProperty(value = "主键")
    @NotBlank(message = "主键不能为空")
    private String id;

    @ApiModelProperty(value = "状态")
    @NotNull(message = "状态不能为空")
    private Byte status;
}
