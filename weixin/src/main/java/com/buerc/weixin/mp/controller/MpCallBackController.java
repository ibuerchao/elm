package com.buerc.weixin.mp.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/mp")
public class MpCallBackController {
    private static final String RAW = "raw"; //明文消息
    private static final String AES = "aes"; //加密消息

    @Resource
    private WxMpMessageRouter router;

    @Resource
    private WxMpService wxMpService;

    @PostMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        if (checkUrl(request, response)) {
            return;
        }
        if (checkSignature(request, response)) {
            return;
        }

        String encryptType = getEncryptType(request);
        if (RAW.equals(encryptType)) {
            raw(request, response);
        } else if (AES.equals(encryptType)) {
            aes(request, response);
        } else {
            response.getWriter().println("不可识别的加密类型");
        }
    }

    // 验证签名
    private boolean checkSignature(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return true;
        }
        return false;
    }

    //验证是否是微信的验证请求
    private boolean checkUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().println(echostr);
            return true;
        }
        return false;
    }

    private String getEncryptType(HttpServletRequest request) {
        String type = request.getParameter("encrypt_type");
        return StringUtils.isBlank(type) ? RAW : type;
    }

    private void raw(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 明文传输的消息
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
        WxMpXmlOutMessage outMessage = router.route(inMessage);
        if (outMessage == null) {
            //为null，说明路由配置有问题，需要注意
            response.getWriter().write("");
            return;
        }
        response.getWriter().write(outMessage.toXml());
    }

    private void aes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        WxMpConfigStorage config = wxMpService.getWxMpConfigStorage();
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String msgSignature = request.getParameter("msg_signature");
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(inputStream, config, timestamp, nonce, msgSignature);
        WxMpXmlOutMessage outMessage = router.route(inMessage);
        if (outMessage == null) {
            //为null，说明路由配置有问题，需要注意
            response.getWriter().write("");
            return;
        }
        response.getWriter().write(outMessage.toEncryptedXml(config));
    }
}