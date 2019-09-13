package com.pollra.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class JwtAspect {

    @Around("@annotation(com.pollra.aop.JwtAuthentication)")
    public Object aspectJwtAuthentication(ProceedingJoinPoint pjp)throws Throwable{
        long begin = System.currentTimeMillis();
        Object retVal = pjp.proceed();  // 메서드 호출 자체를 감쌈
        log.info((System.currentTimeMillis() - begin)+"");
        return retVal;
    }

    @Around("@annotation(com.pollra.aop.JwtAuthorization)")
    public Object aspectJwtAuthorization(ProceedingJoinPoint pjp)throws Throwable{
        long begin = System.currentTimeMillis();
        Object retVal = pjp.proceed();  // 메서드 호출 자체를 감쌈
        log.info((System.currentTimeMillis() - begin)+"");
        return retVal;
    }
}
