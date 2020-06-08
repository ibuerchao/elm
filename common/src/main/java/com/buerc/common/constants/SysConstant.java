package com.buerc.common.constants;

public interface SysConstant {
    //权限编码格式  应用模块:菜单模块:功能模块:操作模块
    interface UserCode {
        String QUERY_USER_LIST = "web:sys:user:list";
        String USER_LOGIN = "web:sys:user:login";
    }

    interface UserStatus {
        Byte FROZEN = 0; //冻结
        Byte NORMAL = 1; //正常
        Byte DELETED = 2; //删除
        Byte NO_ACTIVATED = 3; //未激活
        Byte LOCKED = 4; //锁定
    }
}
