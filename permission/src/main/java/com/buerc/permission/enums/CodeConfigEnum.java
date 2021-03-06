package com.buerc.permission.enums;

import com.buerc.Config;

public enum CodeConfigEnum {
    TEST("test", "123", 100, 10, true),
    USER("web:sys:user", "+12345", 500, 50, true),
    DEPT("web:sys:dept", "+123", 100, 10, true),
    MODULE("web:sys:module", "+123", 100, 10, true),
    RES("web:sys:res", "+123", 100, 10, true),
    ROLE("web:sys:role", "+1234", 100, 10, true),
    ROLE_RES("web:sys:role_res", "+123456", 500, 10, true),
    ROLE_USER("web:sys:role_user", "+123456", 500, 10, true),
    OPER_LOG("web:sys:oper_log", "+12345", 500, 10, true),
    ;

    private String key;
    private String pattern;
    private int cacheSize;
    private int leftSize;
    private boolean cycle;

    CodeConfigEnum(String key, String pattern, int cacheSize, int leftSize, boolean cycle) {
        this.key = key;
        this.pattern = pattern;
        this.cacheSize = cacheSize;
        this.leftSize = leftSize;
        this.cycle = cycle;
    }

    public static Config getConfig(CodeConfigEnum e) {
        return new Config(e.key, e.pattern, e.cacheSize, e.leftSize, e.cycle);
    }

    public String getKey() {
        return key;
    }
}
