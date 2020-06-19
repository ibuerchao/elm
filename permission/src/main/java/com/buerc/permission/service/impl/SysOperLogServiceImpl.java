package com.buerc.permission.service.impl;

import com.buerc.permission.mapper.SysOperLogMapper;
import com.buerc.permission.model.SysOperLog;
import com.buerc.permission.service.SysOperLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysOperLogServiceImpl implements SysOperLogService {

    @Resource
    private SysOperLogMapper sysOperLogMapper;

    @Override
    public void insertSelective(SysOperLog log) {
        sysOperLogMapper.insertSelective(log);
    }
}
