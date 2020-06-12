package com.buerc.security.holder;

import com.buerc.common.constants.RedisConstant;
import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.ApplicationContextUtil;
import com.buerc.common.utils.JSONUtil;
import com.buerc.common.utils.JwtTokenUtil;
import com.buerc.common.vo.permission.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityContextHolder {
    private static final JwtTokenUtil jwtTokenUtil = ApplicationContextUtil.getApplicationContext().getBean(JwtTokenUtil.class);
    private static final RestTemplate restTemplate = ApplicationContextUtil.getApplicationContext().getBean(RestTemplate.class);
    private static final ConcurrentHashMap<String, UserInfo> map = new ConcurrentHashMap<>();
    private static final String url = ApplicationContextUtil.getApplicationContext().getBean(AppProperties.class).getPermissionServiceUrl()+"/help/info?token=%s";

    public static UserInfo get(){
        return getUserInfo(getCurrentToken());
    }

    public static String getUserId(){
        return get().getInfo().getId();
    }

    public static String getUserName(){
        return get().getInfo().getUsername();
    }

    private static UserInfo getUserInfo(String token) {
        String userId = jwtTokenUtil.getUserId(token);
        UserInfo userInfo = map.get(userId);
        if (userInfo == null){
            userInfo = getUserInfoByHttp(token);
            map.put(userId,userInfo);
        }
        return userInfo;
    }

    private static UserInfo getUserInfoByHttp(String token){
        String requestUrl = String.format(url, token);
        ResponseEntity<HashMap> exchange;
        try{
            HttpHeaders header = new HttpHeaders();
            header.add(RedisConstant.PARAM_HEADER,token);

            HttpEntity<String> httpEntity = new HttpEntity<>(null, header);
            exchange = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, HashMap.class);
        }catch (Exception e){
            throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        if (exchange == null || exchange.getStatusCodeValue()!=HttpStatus.OK.value()
                || !exchange.hasBody()|| exchange.getBody().get("code") == null
                || !exchange.getBody().get("code").equals(200)){
            throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        UserInfo userInfo = JSONUtil.toObject(JSONUtil.toStr(exchange.getBody().get("data")), UserInfo.class);
        if (userInfo == null){
            throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
        }
        return userInfo;
    }

    private static String getCurrentToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(RedisConstant.PARAM_HEADER);
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(RedisConstant.PARAM_HEADER);
        }
        return token;
    }
    //todo 监听用户信息变更后重新加载缓存
}