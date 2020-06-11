package com.buerc.common.utils;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;

public class JwtTokenUtil {
    private static Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.refreshTime}")
    private int refreshTime;

    public Long getExpiration() {
        return expiration;
    }

    /**
     * 解析token
     * @param token token
     * @return
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException eje) {
            log.error("===== token过期 =====", eje);
            throw new BizException(ResultCode.TOKEN_EXPIRED_CODE,ResultCode.TOKEN_EXPIRED_MSG);
        } catch (Exception e){
            log.error("===== token解析异常 =====", e);
            throw new BizException(ResultCode.TOKEN_INVALID_CODE,ResultCode.TOKEN_INVALID_MSG);
        }
    }

    /**
     * 生成token
     * @param claims token需要包含的信息
     * @return
     */
    public String createToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成token
     * @param id 用户id
     * @return
     */
    public String createToken(String id){
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成指定过期时间的token
     */
    public String createToken(String id,long time){
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + time * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserId(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * token是否过期
     * @param token
     * @return
     */
    public boolean isExpiration(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    /**
     * 刷新token
     * @param oldToken
     * @return
     */
    public String refreshToken(String oldToken) {
        if(StringUtils.isBlank(oldToken)){
            return null;
        }
        //token校验不通过
        Claims claims = parseToken(oldToken);
        if(claims==null){
            return null;
        }
        //如果token已经过期，不支持刷新
        if(isExpiration(oldToken)){
            return null;
        }
        //如果token在指定时间之内刚刷新过，返回原token
        if(tokenRefreshJustBefore(oldToken,refreshTime)){
            return oldToken;
        }else{
            claims.put(CLAIM_KEY_CREATED, new Date());
            return createToken(claims);
        }
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     * @param token 原token
     * @param time 指定时间（秒）
     */
    private boolean tokenRefreshJustBefore(String token, int time) {
        Claims claims = parseToken(token);
        Date created = claims.get(CLAIM_KEY_CREATED, Date.class);
        Date refreshDate = new Date();

        return refreshDate.after(created) && refreshDate.before(DateUtils.addMinutes(created, time));
    }

}
