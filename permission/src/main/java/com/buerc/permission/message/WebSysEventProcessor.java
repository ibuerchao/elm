package com.buerc.permission.message;

import com.buerc.redis.Event;
import com.buerc.redis.EventProcessor;
import com.buerc.redis.constants.EventConstants;
import org.springframework.stereotype.Component;

@Component
public class WebSysEventProcessor implements EventProcessor {
    @Override
    public Integer getModule() {
        return EventConstants.Module.WEB_SYS;
    }

    @Override
    public void process(Event event) {
        if (event.getType() == EventConstants.Type.ROLE_USER) {
            System.out.println("EventConstants.Type.ROLE_USER");
        }else if(event.getType() == EventConstants.Type.ROLE_RES){
            System.out.println("EventConstants.Type.ROLE_RES");
        }
    }
}
