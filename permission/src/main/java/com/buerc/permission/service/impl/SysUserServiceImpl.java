package com.buerc.permission.service.impl;

import com.buerc.common.constants.RedisConstant;
import com.buerc.common.constants.ResultCode;
import com.buerc.common.constants.SysConstant;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.JwtTokenUtil;
import com.buerc.common.utils.RedisUtil;
import com.buerc.common.utils.RsaUtil;
import com.buerc.permission.mapper.SysUserMapper;
import com.buerc.permission.model.SysUser;
import com.buerc.permission.model.SysUserExample;
import com.buerc.permission.param.Mail;
import com.buerc.permission.param.SignUp;
import com.buerc.permission.param.User;
import com.buerc.permission.service.SysUserService;
import com.buerc.permission.util.IpUtil;
import com.buerc.permission.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private MailUtil mailUtil;

    @Resource
    private RsaUtil rsaUtil;

    @Override
    public String signUp(SignUp signUp) {
        validateSignUp(signUp);
        String id = insertSignUp(signUp);
        sendMail(signUp,id);
        String token = jwtTokenUtil.createToken(id);
        RedisUtil.set(RedisConstant.USER_TOKEN.concat(id),token,jwtTokenUtil.getExpiration(),TimeUnit.SECONDS);
        return token;
    }

    /**
     * 验证注册时用户名或邮箱是否被占用
     */
    private void validateSignUp(SignUp signUp){
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(signUp.getUsername());
        if(sysUserMapper.countByExample(example) > 0){
            throw new BizException(ResultCode.USER_EXIST_ERROR_CODE,ResultCode.USER_EXIST_ERROR_MSG);
        }
        example.clear();
        criteria = example.createCriteria();
        criteria.andMailEqualTo(signUp.getMail());
        if(sysUserMapper.countByExample(example) > 0){
            throw new BizException(ResultCode.MAIL_EXIST_ERROR_CODE,ResultCode.MAIL_EXIST_ERROR_MSG);
        }
    }

    /**
     * 插入注册信息
     * @param signUp 注册信息
     * @return 主键id
     */
    private String insertSignUp(SignUp signUp){
        SysUser user = new SysUser();
        BeanUtils.copyProperties(signUp,user);
        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setStatus(SysConstant.UserStatus.NO_ACTIVATED);
        user.setOperateId(id);
        user.setOperateName(signUp.getUsername());
        user.setOperateTime(new Date());
        user.setOperateIp(IpUtil.getRemoteAddr());
        user.setTelephone("13554031905");
        user.setDeptId("==");
        if (sysUserMapper.insert(user) != 1){
            throw new BizException(ResultCode.INTERNAL_ERROR_CODE,ResultCode.INTERNAL_ERROR_MSG);
        }
        return id;
    }

    /**
     * 注册成功后发送邮件激活通知
     */
    private void sendMail(SignUp signUp,String id){
        Mail mail = new Mail();
        mail.setEmail(signUp.getMail());
        mail.setTitle("不二超elm注册成功通知");
        Map<String,Object> map = new HashMap<>();
        map.put("username",signUp.getUsername());
        String token = jwtTokenUtil.createToken(id);
        RedisUtil.set(RedisConstant.SIGN_UP_TOKEN.concat(id),token,5,TimeUnit.MINUTES);
        String alink = IpUtil.getServerAddr().concat("/api/validate_email?payload=").concat(token);
        map.put("alink",alink);
        mail.setAttachment(map);
        mailUtil.sendTemplateMail(mail,"mail");
    }

    public Map<String,Object> validateEmail(String payload){
        Map<String,Object> map = new HashMap<>();
        String id;
        //1.验证参数token有效性
        try {
            id = jwtTokenUtil.getUserId(payload);
        }catch (BizException e){
            int code = e.getCode();
            if (ResultCode.TOKEN_EXPIRED_CODE == code){
                map.put("flag",0);//邮箱激活参数token已过期
            }
            if (ResultCode.TOKEN_INVALID_CODE == code){
                map.put("flag",1);//邮箱激活参数token非法
            }
            return map;
        }
        String value = RedisUtil.get(RedisConstant.SIGN_UP_TOKEN.concat(id));
        if (StringUtils.isBlank(value)){
            map.put("flag",0);//redis缓存已清空
        }else{
            SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
            if (SysConstant.UserStatus.NORMAL.equals(sysUser.getStatus())){
                map.put("flag",2);//2.用户已激活此账号
            }else{
                SysUser user = new SysUser();
                user.setId(id);
                user.setStatus(SysConstant.UserStatus.NORMAL);
                user.setOperateTime(new Date());
                user.setOperateIp(IpUtil.getRemoteAddr());
                int i = sysUserMapper.updateByPrimaryKeySelective(user);
                if (i == 1){//更新成功
                    map.put("flag",3);
                    map.put("username",sysUser.getUsername());
                } else {//更新失败
                    map.put("flag",4);
                }
            }
        }
        return map;
    }

    @Override
    public List<SysUser> selectByExample(SysUserExample example) {
        return sysUserMapper.selectByExample(example);
    }

    @Override
    public SysUser selectByPrimaryKey(String id){
        return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public Set<String> getRolesByUserId(String id) {
        Set<String> roles = new HashSet<>();
        roles.add("select");
        return roles;
    }

    @Override
    public Set<String> getPermissionsByUserId(String id) {
        return new HashSet<>();
    }

    @Override
    public String login(User user) {
        String decrypt = rsaUtil.decryptByPrivateKey(user.getPassword());
        SysUserExample sysUserExample = new SysUserExample();
        SysUserExample.Criteria criteria = sysUserExample.createCriteria();
        criteria.andUsernameEqualTo(user.getUsername());
        criteria.andPasswordEqualTo(decrypt);
        List<SysUser> users = sysUserMapper.selectByExample(sysUserExample);
        if (CollectionUtils.isEmpty(users)){
            throw new BizException(ResultCode.USER_OR_PASSWORD_ERROR_CODE,ResultCode.USER_OR_PASSWORD_ERROR_MSG);
        }
        if (users.size()>1){
            throw new BizException(ResultCode.INTERNAL_ERROR_CODE,ResultCode.INTERNAL_ERROR_MSG);
        }
        SysUser sysUser = users.get(0);
        if (!SysConstant.UserStatus.NORMAL.equals(sysUser.getStatus())){
            throw new BizException(ResultCode.USER_STATUS_ERROR_CODE,ResultCode.USER_STATUS_ERROR_MSG);
        }
        String id = sysUser.getId();
        String key = RedisUtil.getKeyForToken(id);
        String value = jwtTokenUtil.createToken(id);
        long time = user.isRememberMe() ? RedisConstant.REMEMBER_ME : jwtTokenUtil.getExpiration();
        RedisUtil.set(key,value,time, TimeUnit.SECONDS);
        return value;
    }

    @Override
    public Boolean logOut() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String header = request.getHeader(RedisConstant.PARAM_HEADER);
        if (StringUtils.isBlank(header)){
            return Boolean.TRUE;
        }
        String userId = jwtTokenUtil.getUserId(header);
        return RedisUtil.del(RedisConstant.USER_TOKEN.concat(userId));
    }

    @Override
    public void getCode(String email) {
        if(StringUtils.isBlank(email)){
            throw new BizException(ResultCode.EMAIL_NOT_BLANK_CODE,ResultCode.EMAIL_NOT_BLANK_MSG);
        }

        Mail mail = new Mail();
        mail.setEmail(email);
        mail.setTitle("vue-web验证码");
        Map<String,Object> map = new HashMap<>();
        map.put("code",123456);
        mail.setAttachment(map);
        mailUtil.sendTemplateMail(mail,"code");
    }
}
