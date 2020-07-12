package com.buerc.permission.service.impl;

import com.buerc.common.constants.RedisConstant;
import com.buerc.common.constants.ResultCode;
import com.buerc.common.constants.SysConstant;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.*;
import com.buerc.permission.config.WebLogAspect;
import com.buerc.permission.mapper.SysUserMapper;
import com.buerc.permission.model.SysUser;
import com.buerc.permission.model.SysUserExample;
import com.buerc.permission.service.SysDeptService;
import com.buerc.permission.service.SysUserService;
import com.buerc.permission.util.IpUtil;
import com.buerc.permission.util.MailUtil;
import com.buerc.sys.bo.UserInfo;
import com.buerc.sys.dto.*;
import com.buerc.sys.vo.UserVo;
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
    private SysDeptService sysDeptService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private MailUtil mailUtil;

    @Resource
    private RsaUtil rsaUtil;

    @Override
    public void signUp(SignUpParam signUp) {
        validateSignUp(signUp);
        String id = insertSignUp(signUp);
        sendMail(signUp,id);
        String token = jwtTokenUtil.createToken(id);
        RedisUtil.set(RedisConstant.USER_TOKEN.concat(id),token,jwtTokenUtil.getExpiration(),TimeUnit.SECONDS);
    }

    /**
     * 验证注册时用户名或邮箱是否被占用
     */
    private void validateSignUp(SignUpParam signUp){
       validateUsername(signUp.getUsername());
       validateEmail(signUp.getEmail());
    }

    /**
     * 插入注册信息
     * @param signUp 注册信息
     * @return 主键id
     */
    private String insertSignUp(SignUpParam signUp){
        String password = validateEncryptPassword(signUp.getPassword());
        SysUser user = new SysUser();
        BeanUtils.copyProperties(signUp,user);
        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setPassword(password);
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
    private void sendMail(SignUpParam signUp,String id){
        MailParam mail = new MailParam();
        mail.setEmail(signUp.getEmail());
        mail.setTitle("不二超elm注册成功通知");
        Map<String,Object> map = new HashMap<>();
        map.put("username",signUp.getUsername());
        String token = jwtTokenUtil.createToken(id);
        RedisUtil.set(RedisConstant.SIGN_UP_TOKEN.concat(id),token,5,TimeUnit.MINUTES);
        String alink = IpUtil.getServerAddr().concat("/api/help/validate_email?payload=").concat(token);
        map.put("alink",alink);
        mail.setAttachment(map);
        mailUtil.sendTemplateMail(mail,"mail");
    }

    public Map<String,Object> validatePayload(String payload){
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
    public String login(LoginParam user) {
        String decrypt = rsaUtil.decryptByPrivateKey(user.getPassword());
        SysUserExample sysUserExample = new SysUserExample();
        SysUserExample.Criteria criteria = sysUserExample.createCriteria();
        criteria.andEmailEqualTo(user.getEmail());
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
        long time = user.isRememberMe() ? RedisConstant.REMEMBER_ME : jwtTokenUtil.getExpiration();
        String value = jwtTokenUtil.createToken(id,time);
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
        ValidateKit.notBlank(email,ResultCode.EMAIL_NOT_BLANK_MSG);
        List<SysUser> users = getSysUserByEmail(email);
        if (CollectionUtils.isNotEmpty(users)){
            MailParam mail = new MailParam();
            mail.setEmail(email);
            mail.setTitle("vue-web验证码");
            Map<String,Object> map = new HashMap<>();
            String code = GenerateStrUtil.code(SysConstant.Sys.EMAIL_CODE_LENGTH);
            map.put("code", code);
            mail.setAttachment(map);
            mailUtil.sendTemplateMail(mail,"code");
            String key = RedisConstant.CODE_EMAIL.concat(users.get(0).getId());
            RedisUtil.set(key,code,RedisConstant.CODE_EMAIL_TIME,TimeUnit.MINUTES);
        }
    }

    private List<SysUser> getSysUserByEmail(String email){
        SysUserExample sysUserExample = new SysUserExample();
        SysUserExample.Criteria criteria = sysUserExample.createCriteria();
        criteria.andEmailEqualTo(email);
        return sysUserMapper.selectByExample(sysUserExample);
    }

    @Override
    public void resetPassword(ResetPasswordParam resetPassword) {
        List<SysUser> users = getSysUserByEmail(resetPassword.getEmail());
        if(CollectionUtils.isNotEmpty(users)){
            String password = validateEncryptPassword(resetPassword.getPassword());
            String id = users.get(0).getId();
            String key = RedisConstant.CODE_EMAIL.concat(id);
            String code = RedisUtil.get(key);
            if (StringUtils.isBlank(code)){
                throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.CODE_FAILURE_MSG);
            }
            if (!code.equals(resetPassword.getCode())){
                throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.CODE_ERROR_MSG);
            }
            SysUser user = new SysUser();
            user.setId(id);
            user.setPassword(password);
            user.setOperateTime(new Date());
            user.setOperateIp(IpUtil.getRemoteAddr());
            sysUserMapper.updateByPrimaryKeySelective(user);
            RedisUtil.del(key);
        }else{
            throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.CODE_FAILURE_MSG);
        }
    }

    private String validateEncryptPassword(String encryptPassword){
        String password = rsaUtil.decryptByPrivateKey(encryptPassword);
        ValidateKit.assertTrue(password.length()<6 || password.length()>18,ResultCode.PASSWORD_LENGTH_ERROR_MSG);
        //todo 密码数字字母下划线符号等
        return password;
    }

    @Override
    public UserInfo info(String token) {
        ValidateKit.notBlank(token,ResultCode.TOKEN_BLANK_MSG);
        String userId = jwtTokenUtil.getUserId(token);
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        ValidateKit.notNull(sysUser,ResultCode.USER_NOT_EXIST_MSG);
        UserInfo userInfo = new UserInfo();
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userInfo.setInfo(userVo);
        userInfo.setRoles(new HashSet<>());
        userInfo.setPermissions(new HashSet<>());
        return userInfo;
    }

    @Override
    public UserVo add(UserFormParam userFormParam) {
        validateUsername(userFormParam.getUsername());
        validateEmail(userFormParam.getEmail());
        validateTelephone(userFormParam.getTelephone());
        sysDeptService.checkIdExist(userFormParam.getDeptId());

        SysUser user = new SysUser();
        BeanUtils.copyProperties(userFormParam,user);
        user.setPassword(subStrPhone(userFormParam.getTelephone()));
        sysUserMapper.insertSelective(user);
        UserVo vo  = new UserVo();
        BeanUtils.copyProperties(user,vo);
        return vo;
    }

    /**
     * 验证手机号是否已存在
     */
    private void validateTelephone(String phone){
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andTelephoneEqualTo(phone);
        if(sysUserMapper.countByExample(example) > 0){
            throw new BizException(ResultCode.USER_EXIST_ERROR_CODE,ResultCode.PHONE_EXIST_ERROR_MSG);
        }
    }

    /**
     * 验证用户名是否已存在
     */
    private void validateUsername(String username){
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        if(sysUserMapper.countByExample(example) > 0){
            throw new BizException(ResultCode.USER_EXIST_ERROR_CODE,ResultCode.USER_EXIST_ERROR_MSG);
        }
    }

    /**
     * 验证邮箱是否已存在
     */
    private void validateEmail(String email){
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo(email);
        if(sysUserMapper.countByExample(example) > 0){
            throw new BizException(ResultCode.MAIL_EXIST_ERROR_CODE,ResultCode.MAIL_EXIST_ERROR_MSG);
        }
    }

    /**
     * 截取手机号后八位作为默认密码
     * @return
     */
    private String subStrPhone(String phone){
        return StringUtils.substring(phone,phone.length()-SysConstant.Sys.DEFAULT_PASSWORD_LENGTH);
    }

    @Override
    public void delete(String id) {
        //todo 用户角色资源
        ValidateKit.assertTrue(StringUtils.isBlank(id),ResultCode.PARAM_ERROR_MSG);
        checkIdExist(id);
        SysUser update = new SysUser();
        update.setId(id);
        update.setStatus(SysConstant.UserStatus.DELETED);
        sysUserMapper.updateByPrimaryKeySelective(update);
    }

    private SysUser checkIdExist(String id){
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        if (sysUser == null) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.USER_NOT_EXIST_MSG);
        }
        return sysUser;
    }

    @Override
    public UserVo edit(UserFormParam param) {
        ValidateKit.notBlank(param.getId(), ResultCode.USER_ID_BLANK_MSG);
        SysUser sysUser = checkIdExist(param.getId());
        if (!sysUser.getUsername().equals(param.getUsername())){
            validateUsername(param.getUsername());
        }
        if (!sysUser.getEmail().equals(param.getEmail())){
            validateEmail(param.getEmail());
        }
        if (!sysUser.getTelephone().equals(param.getTelephone())){
            validateTelephone(param.getTelephone());
        }
        SysUser update = new SysUser();
        BeanUtils.copyProperties(param, update);
        sysUserMapper.updateByPrimaryKeySelective(update);
        WebLogAspect.fillTextValue(sysUser,update);
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(update, vo);
        return vo;
    }

    @Override
    public UserVo detail(String id) {
        SysUser user = checkIdExist(id);
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public boolean updateStatus(UpdateStatusParam param) {
        checkStatus(param.getStatus());
        checkIdExist(param.getId());
        SysUser sysUser = new SysUser();
        sysUser.setId(param.getId());
        sysUser.setStatus(param.getStatus());
        int update = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        return update > 0;
    }

    private void checkStatus(Byte status){
        ArrayList<Byte> list = new ArrayList<>();
        list.add(SysConstant.UserStatus.FROZEN);
        list.add(SysConstant.UserStatus.NORMAL);
        list.add(SysConstant.UserStatus.DELETED);
        list.add(SysConstant.UserStatus.NO_ACTIVATED);
        list.add(SysConstant.UserStatus.LOCKED);
        ValidateKit.assertTrue(!list.contains(status),ResultCode.USER_STATUS_INVALID_MSG);
    }
}
