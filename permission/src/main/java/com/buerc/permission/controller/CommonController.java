package com.buerc.permission.controller;

import com.buerc.permission.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping
@Slf4j
public class CommonController {
    @Resource
    private SysUserService sysUserService;

    @GetMapping("/unauthorized")
    public String unauthorized(@RequestParam(value = "code",required = false) String code,
                               @RequestParam(value = "msg",required = false) String msg){
        return "mail";
//        return Result.error(NumberUtils.toInt(code),msg);
    }

    @GetMapping("/validate_email")
    public ModelAndView validateEmail(@RequestParam(value = "payload") String payload){
        return new ModelAndView("validate_email",sysUserService.validateEmail(payload));
    }

}
