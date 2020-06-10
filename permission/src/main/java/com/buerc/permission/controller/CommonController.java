package com.buerc.permission.controller;

import com.buerc.common.web.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
@Slf4j
public class CommonController {

    @ApiOperation(value = "shiro认证未通过时重定向返回错误码")
    @GetMapping("/unauthorized")
    @ResponseBody
    public Result unauthorized(@RequestParam(value = "code",required = false) String code,
                               @RequestParam(value = "msg",required = false) String msg){
        return Result.error(NumberUtils.toInt(code),msg);
    }
}
