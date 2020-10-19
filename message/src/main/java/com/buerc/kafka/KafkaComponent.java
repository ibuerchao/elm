package com.buerc.kafka;

import com.buerc.common.utils.JSONUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class KafkaComponent<T> {
    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;

    public void send(String topic,String type,T data){
        KafkaMessage<T> message = new KafkaMessage<>();
        message.setTopic(topic);
        message.setType(type);
        message.setData(data);
        kafkaTemplate.send(topic, JSONUtil.toStr(message));
    }
}
