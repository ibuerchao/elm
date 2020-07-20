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
public class ResFormParam {
    @ApiModelProperty(value = "权限主键(编辑时必传)")
    private String id;

    @ApiModelProperty(value = "权限编码")
    @NotBlank(message = "权限编码不能为空")
    @Size(min = 1,max = 50,message = "权限编码长度在 1 到 50 个字符")
    private String code;

    @ApiModelProperty(value = "权限名称")
    @NotBlank(message = "权限名称不能为空")
    @Size(min = 1,max = 20,message = "权限名称长度在 1 到 20 个字符")
    private String name;

    @ApiModelProperty(value = "权限所在的权限模块id")
    private String moduleId = SysConstant.Sys.DEFAULT_RES_MODULE_ID;

    @ApiModelProperty(value = "请求的url, 可以填正则表达式")
    private String url;

    @ApiModelProperty(value = "类型，1：菜单，2：按钮，3：其他")
    @NotNull(message = "类型不能为空")
    @Range(min = 1,max = 3,message = "类型可选值 1：菜单，2：按钮，3：其他")
    private Byte type;

    @ApiModelProperty(value = "状态，1：正常，0：冻结")
    @NotNull(message = "状态不能为空")
    @Range(min = 0,max = 1,message = "状态可选值 1：正常，0：冻结")
    private Byte status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
