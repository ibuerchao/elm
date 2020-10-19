package com.buerc.log.sys.service.impl;

import com.buerc.log.mapper.SysOperLogMapper;
import com.buerc.log.model.SysOperLog;
import com.buerc.log.sys.service.SysOperLogService;
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
