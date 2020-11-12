package com.buerc.weixin.mp.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/mp")
public class TemplateController {

    @Resource
    private WxMpService wxMpService;

    @PostMapping("/template/send")
    @ResponseBody
    public String send(@RequestBody Map<String,Object> map) throws WxErrorException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser("okDJF6-Yi9n1KhB_l_x8DyQRnsoc")
                .templateId("hzqjgJRePXK5ms1iyTC6O-JhvtaGb0iS_-GDM3BNwlc")
                .url("http://www.baidu.com")
                .build();
        templateMessage.addData(new WxMpTemplateData("first", dateFormat.format(new Date()), "#FF00FF"))
                .addData(new WxMpTemplateData("remark", RandomStringUtils.randomAlphanumeric(100), "#FF00FF"));

        String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        return msgId;
    }
}
