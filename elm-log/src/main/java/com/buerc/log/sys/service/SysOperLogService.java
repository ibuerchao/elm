package com.buerc.log.sys.service;

import com.buerc.log.model.SysOperLog;

public interface SysOperLogService {
    void insertSelective(SysOperLog log);
}
