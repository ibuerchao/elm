package com.buerc.permission.param;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册实体
 */
@Data
public class SignUp {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3,max = 16,message = "用户名长度在 3 到 16 个字符")
    private String username;
    @Email
    private String email;
    @NotBlank
    private String password;
}
