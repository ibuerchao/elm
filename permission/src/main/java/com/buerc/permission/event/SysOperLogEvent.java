package com.buerc.permission.event;

import org.springframework.context.ApplicationEvent;

public class SysOperLogEvent extends ApplicationEvent {
    public SysOperLogEvent(Object source) {
        super(source);
    }
}
