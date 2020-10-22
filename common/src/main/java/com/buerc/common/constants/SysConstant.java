package com.buerc.common.constants;

public interface SysConstant {
    //只查询一条记录
    int OFFSET = 0;
    int LIMIT = 1;

    //每一级长度
    int LEVEL_LENGTH = 5;
    //左填充
    char PADDING = '0';
    //最大层级
    int MAX_LEVELS = 40;

    String SUPER_ADMIN = "SUPER_ADMIN";

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

    interface ResModuleStatus {
        Byte FORBID = 0; //禁用
        Byte NORMAL = 1; //正常
    }

    interface RoleResTargetType{
        Byte ORG = 1; //组织
        Byte MODULE = 2; //模块
        Byte RES = 3; //资源
    }

    interface RoleUserTargetType{
        Byte ROLE_USER = 1; //为角色id指定用户
        Byte USER_ROLE = 2; //为用户id指定角色
    }

    interface ResType{
        Byte MENU = 1; // 菜单
        Byte BUTTON = 2; //按钮
        Byte OTHER = 3; //其他
    }
}
