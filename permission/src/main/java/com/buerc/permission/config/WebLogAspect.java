package com.buerc.permission.config;

import com.buerc.CodeUtil;
import com.buerc.common.annotation.OperateLog;
import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.DateUtil;
import com.buerc.common.utils.JSONUtil;
import com.buerc.kafka.KafkaComponent;
import com.buerc.kafka.KafkaConstants;
import com.buerc.permission.enums.CodeConfigEnum;
import com.buerc.permission.model.SysOperLog;
import com.buerc.permission.util.IpUtil;
import com.buerc.security.holder.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Aspect
@Component
@Slf4j
@Order(1000)
public class WebLogAspect {

    @Resource
    private KafkaComponent<SysOperLog> kafkaComponent;

    private static ThreadLocal<SysOperLog> threadLocal = new ThreadLocal<>();

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
        sysOperLog.setId(CodeUtil.getCode(CodeConfigEnum.OPER_LOG.getKey(), DateUtil.formatLongCompact()));
        sysOperLog.setOperateId(SecurityContextHolder.getUserId());
        sysOperLog.setOperateName(SecurityContextHolder.getUserName());
        sysOperLog.setOperateIp(IpUtil.getRemoteAddr());
        sysOperLog.setOperateTime(new Date());
    }

    @AfterReturning(returning = "ret", value = "onMethod() || onClass()")
    public void afterReturning(Object ret) {
        try {
            SysOperLog sysOperLog = threadLocal.get();
            sysOperLog.setResult(JSONUtil.toStr(ret));
            kafkaComponent.send(KafkaConstants.Topic.OPERATE_LOG,KafkaConstants.Type.WEB_SYS_API,sysOperLog);
        } finally {
            threadLocal.remove();
        }
    }

    @AfterThrowing(throwing = "throwable", value = "onMethod() || onClass()")
    public void afterThrowing(Throwable throwable) {
        try {
            SysOperLog sysOperLog = threadLocal.get();
            sysOperLog.setResult(ExceptionUtils.getStackTrace(throwable));
            kafkaComponent.send(KafkaConstants.Topic.OPERATE_LOG,KafkaConstants.Type.WEB_SYS_API,sysOperLog);
        } finally {
            threadLocal.remove();
        }
    }

    public static void fillTextValue(Object oldValue,Object newValue){
        SysOperLog sysOperLog = threadLocal.get();
        if (Objects.nonNull(oldValue)) {
            sysOperLog.setOldValue(JSONUtil.toStr(oldValue));
        }
        if (Objects.nonNull(newValue)) {
            sysOperLog.setNewValue(JSONUtil.toStr(newValue));
        }
    }
}
