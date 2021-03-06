package com.buerc.weixin.mp.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping("/mp/menu")
public class MenuController {

    @Resource
    private WxMpService wxMpService;

    @PostMapping("/get")
    @ResponseBody
    public void info() throws WxErrorException {
        wxMpService.getWxMpConfigStorage().updateAccessToken("39_XNY3n_E6V0VmUq3DVpDTKf5ttPBfMrXQZIq_cswdBFyhiVYIv4WpOAVm82W7fqZeRV9fwiBRBiOZ9cEsAJk3iRhwF5W-ZLVhBVJ51dXV0qp5yiwB6Hq_MN2N0v751G7FUPRjkIH20XaFxLrYVNPfAGAZRT",11111111);
        WxMpMenu wxMpMenu = wxMpService.getMenuService().menuGet();
        log.info(wxMpMenu.toJson());
    }

    @PostMapping("/create")
    @ResponseBody
    public void create() throws WxErrorException {
        WxMenu menu = new WxMenu();
        WxMenuButton button1 = new WxMenuButton();
        button1.setType("click");
        button1.setName("今日歌曲");
        button1.setKey("V1001_TODAY_MUSIC");

        WxMenuButton button2 = new WxMenuButton();
        button2.setType("click");
        button2.setName("歌手简介");
        button2.setKey("V1001_TODAY_SINGER");

        WxMenuButton button3 = new WxMenuButton();
        button3.setName("菜单");

        menu.getButtons().add(button1);
        menu.getButtons().add(button2);
        menu.getButtons().add(button3);

        WxMenuButton button31 = new WxMenuButton();
        button31.setType("view");
        button31.setName("搜索");
        button31.setUrl("http://www.soso.com/");

        WxMenuButton button32 = new WxMenuButton();
        button32.setType("view");
        button32.setName("视频");
        button32.setUrl("http://v.qq.com/");

        WxMenuButton button33 = new WxMenuButton();
        button33.setType("click");
        button33.setName("赞一下我们");
        button33.setKey("V1001_GOOD");

        button3.getSubButtons().add(button31);
        button3.getSubButtons().add(button32);
        button3.getSubButtons().add(button33);
        String s = wxMpService.getMenuService().menuCreate(menu);
        log.info(s);
    }
}
