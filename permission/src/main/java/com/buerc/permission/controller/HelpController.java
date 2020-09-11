package com.buerc.permission.controller;

import com.buerc.common.annotation.ParamValid;
import com.buerc.common.web.Result;
import com.buerc.permission.service.SysUserService;
import com.buerc.sys.bo.UserInfo;
import com.buerc.sys.dto.LoginParam;
import com.buerc.sys.dto.ResetPasswordParam;
import com.buerc.sys.dto.SignUpParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("/help")
@ParamValid
public class HelpController {
    @Resource
    private SysUserService sysUserService;

    @ApiOperation(value = "用户注册")
    @PostMapping("/sign_up")
    @ResponseBody
    public Result signUp(@RequestBody SignUpParam signUp){
        sysUserService.signUp(signUp);
        return Result.success();
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/sign_in")
    @ResponseBody
    public Result sign_in(@RequestBody LoginParam user){
        return Result.success(sysUserService.login(user));
    }

    @ApiOperation(value = "用户登出")
    @PostMapping("/sign_out")
    public Result sign_out(){
        return Result.success(sysUserService.logOut());
    }

    @ApiOperation(value = "重置密码")
    @PostMapping("/reset/password")
    @ResponseBody
    public Result resetPassword(@RequestBody ResetPasswordParam resetPassword){
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
        return new ModelAndView("validate_email",sysUserService.validatePayload(payload));
    }

    @ApiImplicitParams(@ApiImplicitParam(name="token",value = "用户token",dataTypeClass = String.class))
    @ApiOperation(value = "通过token查询用户信息、角色、权限")
    @GetMapping("/info")
    @ResponseBody
    public Result<UserInfo> info(@RequestParam("token") String token){
        return Result.success(sysUserService.info(token));
    }
}
