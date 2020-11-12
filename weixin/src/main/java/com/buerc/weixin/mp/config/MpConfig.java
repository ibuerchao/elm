package com.buerc.weixin.mp.config;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MpConfig {
    @Value("${weixin.mp.appId}")
    private String appId;

    @Value("${weixin.mp.sercret}")
    private String sercret;

    @Value("${weixin.mp.token}")
    private String token;

    @Value("${weixin.mp.aeskey}")
    private String aeskey;

    @Bean
    public WxMpDefaultConfigImpl wxMpDefaultConfig(){
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(appId);
        config.setSecret(sercret);
        config.setToken(token);
        return config;
    }

    @Bean
    public WxMpService wxMpService(){
        WxMpService wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(wxMpDefaultConfig());
        return wxService;
    }

    //打印日志
    @Bean
    public WxMpMessageHandler logHandler(){
        return (wxMpXmlMessage, map, wxMpService, wxSessionManager) -> {
            log.info("转换后的消息：{}",wxMpXmlMessage.toString());
            return null;
        };
    }

    //文本消息处理
    @Bean
    public WxMpMessageHandler textHandler(){
        return (wxMpXmlMessage, map, wxMpService, wxSessionManager) -> WxMpXmlOutMessage.TEXT()
                .content("这是文本消息")
                .fromUser(wxMpXmlMessage.getToUser())
                .toUser(wxMpXmlMessage.getFromUser())
                .build();
    }

    //语音消息处理
    @Bean
    public WxMpMessageHandler voiceHandler(){
        return (wxMpXmlMessage, map, wxMpService, wxSessionManager) -> WxMpXmlOutMessage.TEXT()
                .content("这是语音消息")
                .fromUser(wxMpXmlMessage.getToUser())
                .toUser(wxMpXmlMessage.getFromUser())
                .build();
    }

    //兜底处理
    @Bean
    public WxMpMessageHandler footerHandler(){
        return (wxMpXmlMessage, map, wxMpService, wxSessionManager) -> WxMpXmlOutMessage.TEXT()
                .content("兜底消息")
                .fromUser(wxMpXmlMessage.getToUser())
                .toUser(wxMpXmlMessage.getFromUser())
                .build();
    }

    @Bean
    public WxMpMessageRouter router(){
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService());
        router.rule().handler(logHandler()).next()
                .rule().async(false).msgType(WxConsts.XmlMsgType.TEXT).handler(textHandler()).end()
                .rule().async(false).msgType(WxConsts.XmlMsgType.VOICE).handler(voiceHandler()).end()
                .rule().async(false).handler(footerHandler()).end();
        return router;
    }
}
