package com.buerc.permission.param;

import lombok.Data;

import java.util.Map;

/**
 * 发送html邮件的实体类
 */
@Data
public class Mail {
    private String title;
    private String email;
    private String content;
    private Map<String, Object> attachment;
}
