package com.buerc.redis.constants;

interface ModuleWebSys {
    //角色用户发生变化
    int ROLE_USER = 1;
    //角色资源发生变化
    int ROLE_RES = 2;
    //角色本身发生变化(删除，状态变化)
    int ROLE_CHANGE = 3;
}