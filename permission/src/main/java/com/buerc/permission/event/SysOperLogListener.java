package com.buerc.permission.event;

import com.buerc.permission.model.SysOperLog;
import com.buerc.permission.service.SysOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class SysOperLogListener {

    @Resource
    private SysOperLogService sysOperLogService;

    @Async("logThreadPool")
    @EventListener(SysOperLogEvent.class)
    public void saveSysLog(SysOperLogEvent event) {
        SysOperLog sysLog = (SysOperLog) event.getSource();
        sysOperLogService.insertSelective(sysLog);
    }
}
