package com.buerc.log.sys.listener;

import com.buerc.common.utils.JSONUtil;
import com.buerc.kafka.KafkaConstants;
import com.buerc.kafka.KafkaMessage;
import com.buerc.log.model.SysOperLog;
import com.buerc.log.sys.service.SysOperLogService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SysLogListener {

    @Resource
    private SysOperLogService sysOperLogService;

    @KafkaListener(topics = {KafkaConstants.Topic.OPERATE_LOG})
    public void process(ConsumerRecord record){
        Object value = record.value();
        KafkaMessage<SysOperLog> message = JSONUtil.toObject(value.toString(), new TypeReference<KafkaMessage<SysOperLog>>() {
        });
        sysOperLogService.insertSelective(message.getData());
    }
}
