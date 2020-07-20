package com.buerc.common.constants;

public interface SysConstant {
    //只查询一条记录
    int OFFSET = 0;
    int LIMIT = 1;

    interface Sys{
        //邮箱验证码长度
        int EMAIL_CODE_LENGTH = 6;
        //默认部门父级ID
        String DEFAULT_DEPT_PARENT_ID = "root";
        //默认权限模块所在ID
        String DEFAULT_RES_MODULE_ID = "root";
        //分页默认起始值
        int OFFSET = 0;
        //分页默认每页大小
        int LIMIT = 10;
        //默认密码长度(截取手机号后八位)
        int DEFAULT_PASSWORD_LENGTH = 8;
    }

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

    interface DeptStatus {
        Byte FORBID = 0; //禁用
        Byte NORMAL = 1; //正常
    }

    interface RoleStatus {
        Byte FORBID = 0; //禁用
        Byte NORMAL = 1; //正常
    }

    interface ResStatus {
        Byte FORBID = 0; //禁用
        Byte NORMAL = 1; //正常
    }
}
