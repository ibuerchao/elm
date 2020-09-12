package com.buerc.sys.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 重置密码实体类
 */
@Data
public class ResetPasswordParam {
    //邮箱
    @Email(message = "邮箱地址不合法")
    @NotBlank(message = "邮箱不能为空")
    private String email;
    //新密码
    @NotBlank(message = "密码不能为空")
    private String password;
    //邮箱验证码
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6,max = 6,message = "验证码长度必须为6")
    private String code;
}
