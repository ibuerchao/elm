package com.buerc.permission.config;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.JSONUtil;
import com.buerc.permission.event.SysOperLogEvent;
import com.buerc.permission.model.SysOperLog;
import com.buerc.permission.util.IpUtil;
import com.buerc.security.holder.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class WebLogAspect {
    private static ThreadLocal<SysOperLog> threadLocal = new ThreadLocal<>();

    @Resource
    private ApplicationContext applicationContext;

    @Pointcut("@annotation(com.buerc.common.annotation.OperateLog)")
    public void onMethod() {
    }

    @Pointcut("@within(com.buerc.common.annotation.OperateLog)")
    public void onClass() {
    }

    @Before(value = "onMethod() || onClass()")
    public void before(JoinPoint joinPoint) {
        OperateLog classAnnotation = joinPoint.getTarget().getClass().getAnnotation(OperateLog.class);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        OperateLog methodAnnotation = methodSignature.getMethod().getAnnotation(OperateLog.class);

        if (classAnnotation == null || methodAnnotation == null) {
            throw new BizException(ResultCode.INTERNAL_ERROR_CODE, ResultCode.INTERNAL_ERROR_MSG);
        }
        SysOperLog sysOperLog = new SysOperLog();
        threadLocal.set(sysOperLog);

        sysOperLog.setModule(classAnnotation.module());
        sysOperLog.setType(methodAnnotation.type());
        sysOperLog.setName(classAnnotation.value() + "-" + methodAnnotation.value());
        sysOperLog.setArgs(JSONUtil.toStr(joinPoint.getArgs()));
        sysOperLog.setId(UUID.randomUUID().toString());
        sysOperLog.setOperateId(SecurityContextHolder.getUserId());
        sysOperLog.setOperateName(SecurityContextHolder.getUserName());
        sysOperLog.setOperateIp(IpUtil.getRemoteAddr());
        sysOperLog.setOperateTime(new Date());
    }

    @AfterReturning(returning = "ret", value = "onMethod() || onClass()")
    public void afterReturning(Object ret) {
        // 结果转换
        SysOperLog sysOperLog = threadLocal.get();
        sysOperLog.setResult(JSONUtil.toStr(ret));
        try {
            applicationContext.publishEvent(new SysOperLogEvent(sysOperLog));
        } finally {
            threadLocal.remove();
        }
    }

    @AfterThrowing(throwing = "throwable", value = "onMethod() || onClass()")
    public void afterThrowing(Throwable throwable) {
        SysOperLog sysOperLog = threadLocal.get();
        sysOperLog.setResult(ExceptionUtils.getStackTrace(throwable));
        try {
            applicationContext.publishEvent(new SysOperLogEvent(sysOperLog));
        } finally {
            threadLocal.remove();
        }
    }
}
