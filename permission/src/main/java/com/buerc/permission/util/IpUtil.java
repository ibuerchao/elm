package com.buerc.permission.util;

import com.alibaba.druid.util.DruidWebUtils;
import com.buerc.common.utils.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * IP工具类
 */
@Slf4j
public class IpUtil {
    /**
     * 获取远程客户端ip
     */
    public static String getRemoteAddr() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return DruidWebUtils.getRemoteAddr(request);
    }

    /**
     * 获取服务器ip
     */
    public static String getServerIp(){
        InetAddress localhost;
        try {
            localhost = Inet4Address.getLocalHost();
        } catch (Exception e) {
            log.error("获取服务器地址失败",e);
            return "";
        }
        return localhost.getHostAddress();
    }

    /**
     * 获取服务器端口
     */
    public static String getServerPort(){
        Environment environment = ApplicationContextUtil.getApplicationContext().getBean(Environment.class);
        return environment.getProperty("local.server.port");
    }

    /**
     * 获取服务地址
     */
    public static String getServerAddr(){
        return "http://".concat(IpUtil.getServerIp()).concat(":").concat(IpUtil.getServerPort());
    }
}
