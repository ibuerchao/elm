package com.buerc.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EventProcessorProvider {
    private static final Map<Integer, EventProcessor> map = new HashMap<>();

    @Autowired
    public void setProviders(List<EventProcessor> list) {
        map.putAll(list.stream().collect(Collectors.toMap(EventProcessor::getModule, Function.identity())));
    }

    void process(Event event) {
        Integer module = event.getModule();
        if (module == null) {
            return;
        }
        EventProcessor eventProcessor = map.get(module);
        if (eventProcessor != null) {
            eventProcessor.process(event);
        }
    }
}
