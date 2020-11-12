package com.buerc.weixin.mp.config;

import com.buerc.weixin.mp.Handler.DefaultWxMpMessageHandler;
import com.buerc.weixin.mp.Handler.LogWxMpMessageHandler;
import com.buerc.weixin.mp.Handler.TextWxMpMessageHandler;
import com.buerc.weixin.mp.Handler.VoiceWxMpMessageHandler;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
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

    @Bean
    public WxMpMessageRouter router(){
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService());
        router.rule().handler(new LogWxMpMessageHandler()).next()
                .rule().async(false).msgType(WxConsts.XmlMsgType.TEXT).handler(new TextWxMpMessageHandler()).end()
                .rule().async(false).msgType(WxConsts.XmlMsgType.VOICE).handler(new VoiceWxMpMessageHandler()).end()
                .rule().async(false).handler(new DefaultWxMpMessageHandler()).end();
        return router;
    }
}
