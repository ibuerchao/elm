package com.buerc.permission.service;

import com.buerc.permission.model.SysOperLog;

public interface SysOperLogService {
    void insertSelective(SysOperLog log);
}
