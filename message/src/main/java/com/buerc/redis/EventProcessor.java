package com.buerc.redis;

public interface EventProcessor {
    Integer getModule();
    void process(Event event);
}
