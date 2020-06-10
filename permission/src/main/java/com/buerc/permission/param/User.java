package com.buerc.permission.param;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {
    @Email
    private String email;
    @NotBlank(message = "密码不能为空")
    private String password;
    //是否七天免密登录
    private boolean rememberMe = false;
}
