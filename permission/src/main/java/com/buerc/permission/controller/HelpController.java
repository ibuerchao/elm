package com.buerc.permission.controller;

import com.buerc.common.utils.BeanValidator;
import com.buerc.common.web.Result;
import com.buerc.permission.param.ResetPassword;
import com.buerc.permission.param.SignUp;
import com.buerc.permission.param.User;
import com.buerc.permission.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("/help")
public class HelpController {
    @Resource
    private SysUserService sysUserService;

    @ApiOperation(value = "用户注册")
    @PostMapping("/sign_up")
    @ResponseBody
    public Result signUp(@RequestBody SignUp signUp){
        BeanValidator.validator(signUp);
        return Result.success(sysUserService.signUp(signUp));
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/sign_in")
    @ResponseBody
    public Result sign_in(@RequestBody User user){
        BeanValidator.validator(user);
        return Result.success(sysUserService.login(user));
    }

    @ApiOperation(value = "重置密码")
    @PostMapping("/reset/password")
    @ResponseBody
    public Result resetPassword(@RequestBody ResetPassword resetPassword){
        BeanValidator.validator(resetPassword);
        sysUserService.resetPassword(resetPassword);
        return Result.success();
    }

    @ApiOperation(value = "获取邮箱验证码")
    @GetMapping("/code")
    @ResponseBody
    public Result code(@RequestParam("email") String email){
        sysUserService.getCode(email);
        return Result.success();
    }

    @ApiOperation(value = "通过链接激活账号")
    @GetMapping("/validate_email")
    public ModelAndView validateEmail(@RequestParam(value = "payload") String payload){
        return new ModelAndView("validate_email",sysUserService.validateEmail(payload));
    }
}
