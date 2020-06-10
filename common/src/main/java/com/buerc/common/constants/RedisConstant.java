package com.buerc.common.constants;

public interface RedisConstant {
    //请求参数头部
    String PARAM_HEADER="token";
    //用户登录时的token
    String USER_TOKEN = "token:user:";
    //用户注册后需要激活时的token
    String SIGN_UP_TOKEN = "token:sign_up:";
    //邮箱验证码
    String CODE_EMAIL = "code_email:";
    //邮箱验证码有效期(分钟)
    Integer CODE_EMAIL_TIME = 15;

    //7天免密登录
    Integer REMEMBER_ME = 7*24*60*60;
}
