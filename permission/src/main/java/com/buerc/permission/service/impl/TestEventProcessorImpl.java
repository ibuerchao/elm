package com.buerc.permission.service.impl;

import com.buerc.common.utils.JSONUtil;
import com.buerc.redis.Event;
import com.buerc.redis.EventProcessor;
import org.springframework.stereotype.Service;

@Service
public class TestEventProcessorImpl implements EventProcessor {

    @Override
    public Integer getModule() {
        return 1;
    }

    @Override
    public void process(Event event) {
        System.out.println(JSONUtil.toStr(event));
    }
}
