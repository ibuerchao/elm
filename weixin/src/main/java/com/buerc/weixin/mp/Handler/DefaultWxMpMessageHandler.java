package com.buerc.weixin.mp.Handler;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import java.util.Map;

/**
 * 默认消息处理器
 */
public class DefaultWxMpMessageHandler implements WxMpMessageHandler {
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage,
                                    Map<String, Object> map,
                                    WxMpService wxMpService,
                                    WxSessionManager wxSessionManager){
        return WxMpXmlOutMessage.TEXT()
                .content("欢迎使用")
                .fromUser(wxMpXmlMessage.getToUser())
                .toUser(wxMpXmlMessage.getFromUser())
                .build();
    }
}
