package com.buerc.kafka;

public interface KafkaConstants {
    interface Topic {
        String OPERATE_LOG = "operate_log";
    }

    interface Type extends OperateLogType {
    }
}
