package com.buerc.permission.config;

import com.buerc.common.annotation.OperateLog;
import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.JSONUtil;
import com.buerc.permission.mapper.SysOperLogMapper;
import com.buerc.permission.model.SysOperLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
@Slf4j
public class WebLogAspect {
    private static ThreadLocal<SysOperLog> threadLocal = new ThreadLocal<>();

    @Resource
    private SysOperLogMapper sysOperLogMapper;

    @Pointcut("@annotation(com.buerc.common.annotation.OperateLog)")
    public void onMethod(){}

    @Pointcut("@within(com.buerc.common.annotation.OperateLog)")
    public void onClass(){}

    @Around(value = "onMethod() || onClass()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        OperateLog classAnnotation = joinPoint.getTarget().getClass().getAnnotation(OperateLog.class);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        OperateLog methodAnnotation = methodSignature.getMethod().getAnnotation(OperateLog.class);

        if (classAnnotation == null || methodAnnotation == null){
            throw new BizException(ResultCode.INTERNAL_ERROR_CODE,ResultCode.INTERNAL_ERROR_MSG);
        }

        SysOperLog sysOperLog = new SysOperLog();
        threadLocal.set(sysOperLog);

        sysOperLog.setModule(classAnnotation.module());
        sysOperLog.setType(methodAnnotation.type());
        sysOperLog.setName(classAnnotation.value() + "-" + methodAnnotation.value());
        sysOperLog.setArgs(JSONUtil.toStr(joinPoint.getArgs()));
        Object retVal  = joinPoint.proceed();
        sysOperLog.setResult(JSONUtil.toStr(retVal));
        return retVal;
    }


    @AfterThrowing(throwing="throwable",value = "onMethod() || onClass()")
    public void afterThrowing(Throwable throwable){
        SysOperLog sysOperLog = threadLocal.get();
        sysOperLog.setResult(ExceptionUtils.getStackTrace(throwable));
        sysOperLogMapper.insertSelective(sysOperLog);
        threadLocal.remove();
    }

    @AfterReturning(value = "onMethod() || onClass()")
    public void afterReturning(){
        SysOperLog sysOperLog = threadLocal.get();
        sysOperLogMapper.insertSelective(sysOperLog);
        threadLocal.remove();
    }
}
