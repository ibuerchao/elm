package com.buerc.sys.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginParam {
    @Email
    private String email;
    @NotBlank(message = "密码不能为空")
    private String password;
    //是否七天免密登录
    private boolean rememberMe = false;
}
