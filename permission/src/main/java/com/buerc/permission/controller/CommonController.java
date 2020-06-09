package com.buerc.permission.controller;

import com.buerc.common.web.Result;
import com.buerc.permission.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping
@Slf4j
public class CommonController {
    @Resource
    private SysUserService sysUserService;

    @GetMapping("/unauthorized")
    @ResponseBody
    public Result unauthorized(@RequestParam(value = "code",required = false) String code,
                               @RequestParam(value = "msg",required = false) String msg){
        return Result.error(NumberUtils.toInt(code),msg);
    }

    @GetMapping("/validate_email")
    public ModelAndView validateEmail(@RequestParam(value = "payload") String payload){
        return new ModelAndView("validate_email",sysUserService.validateEmail(payload));
    }

    @GetMapping("/code")
    @ResponseBody
    public Result code(@RequestParam("email") String email){
        sysUserService.getCode(email);
        return Result.success();
    }

}
