package com.buerc.common.constants;

public interface RedisConstant {
    //请求参数头部
    String PARAM_HEADER="token";
    //用户登录时的token
    String USER_TOKEN = "token:user:";
    //用户注册后需要激活时的token
    String SIGN_UP_TOKEN = "token:sign_up:";
}
