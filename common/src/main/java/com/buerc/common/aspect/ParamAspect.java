package com.buerc.common.aspect;

import com.buerc.common.utils.BeanValidator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(999)
@Component
public class ParamAspect {

    @Pointcut("@annotation(com.buerc.common.annotation.ParamValid)")
    public void onMethod() {
    }

    @Pointcut("@within(com.buerc.common.annotation.ParamValid)")
    public void onClass() {
    }

    @Before(value = "onMethod() || onClass()")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (Object o : args) {
                BeanValidator.validator(o);
            }
        }
    }
}
