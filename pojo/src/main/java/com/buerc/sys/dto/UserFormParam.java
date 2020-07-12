package com.buerc.sys.dto;

import com.buerc.common.annotation.Phone;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserFormParam {
    @ApiModelProperty(value = "用户主键(编辑时必传)")
    private String id;

    @ApiModelProperty(value = "用户名称")
    @NotBlank(message = "用户名称不能为空")
    @Size(min = 1,max = 20,message = "用户名称长度在 1 到 20 个字符")
    private String username;

    @ApiModelProperty(value = "手机号")
    @Phone
    private String telephone;

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式非法")
    private String email;

    @ApiModelProperty(value = "所在部门")
    @NotBlank(message = "所在部门不能为空")
    private String deptId;

    @ApiModelProperty(value = "用户状态 0冻结 1正常 2删除 3未激活 4锁定")
    @NotNull(message = "状态不能为空")
    @Range(min = 0,max = 4,message = "用户状态只能 0-4之间")
    private Byte status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
