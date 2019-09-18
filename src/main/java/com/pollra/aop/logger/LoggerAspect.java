package com.pollra.aop.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class LoggerAspect {

    @Around("@annotation(com.pollra.aop.logger.anno.ExceptionAutoLogger)")
    public Object exceptionAutoLogger(ProceedingJoinPoint pjp) throws Throwable{
        Object retVal = null;
        try {
            retVal = pjp.proceed();
        }catch (Throwable e){
            log.warn("[");
        }
        return retVal;
    }
}
