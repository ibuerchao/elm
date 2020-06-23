package com.buerc.common.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface OperateLog {
    /**
     * 接口说明
     */
    String value() default "";

    /**
     * 日志模块
     * 1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系
     */
    byte module() default 0;

    /**
     * 操作类型
     * 1.增 2.删 3.改 4.查 5.列表 6.上移 7.下移 8.树
     */
    byte type() default 0;
}
