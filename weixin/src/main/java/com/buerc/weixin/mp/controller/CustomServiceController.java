package com.buerc.weixin.mp.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.request.WxMpKfAccountRequest;
import me.chanjar.weixin.mp.bean.kefu.result.WxMpKfList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/mp/custom_service")
public class CustomServiceController {

    @Resource
    private WxMpService wxMpService;

    @PostMapping("/list")
    @ResponseBody
    public WxMpKfList add() throws WxErrorException {
        return this.wxMpService.getKefuService().kfList();
    }

    @PostMapping("/add")
    @ResponseBody
    public boolean add(Map<String,String> map) throws WxErrorException {
        String kfAccount = map.get("kfAccount");
        String nickName = map.get("nickName");
        WxMpKfAccountRequest request = WxMpKfAccountRequest.builder()
                .kfAccount(kfAccount).nickName(nickName).build();
        return wxMpService.getKefuService().kfAccountAdd(request);
    }
}
