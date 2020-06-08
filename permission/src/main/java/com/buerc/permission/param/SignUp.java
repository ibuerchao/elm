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
    @Size(min = 1,max = 20)
    private String username;
    @Email
    private String mail;
    @NotBlank
    private String password;
}
