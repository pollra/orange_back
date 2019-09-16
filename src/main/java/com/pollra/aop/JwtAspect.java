package com.pollra.aop;

import com.pollra.aop.jwt.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class JwtAspect {

    private JwtService jwtService;

    public JwtAspect(JwtService jwtService) {
        this.jwtService = jwtService;
    }

//    @Around("@annotation(com.pollra.aop.JwtAuthentication)")
//    public Object aspectJwtAuthentication(ProceedingJoinPoint pjp)throws Throwable{
//        long begin = System.currentTimeMillis();
//        Object retVal = pjp.proceed();  // 메서드 호출 자체를 감쌈
//        log.info((System.currentTimeMillis() - begin)+"");
//        return retVal;
//    }

//    @Around("@annotation(com.pollra.aop.JwtAuthorization)")
//    public Object aspectJwtAuthorization(ProceedingJoinPoint pjp)throws Throwable{
//        long begin = System.currentTimeMillis();
//        Object retVal = pjp.proceed();  // 메서드 호출 자체를 감쌈
//        log.info((System.currentTimeMillis() - begin)+"");
//        return retVal;
//    }

    /**
     * 인가 ( 로그인 )
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.pollra.aop.jwt.anno.TokenCredential)")
    public Object tokenCredential(ProceedingJoinPoint pjp) throws Throwable{
        long begin = System.currentTimeMillis();
        jwtService.credential();
        Object retVal = pjp.proceed();
//        log.info((System.currentTimeMillis() - begin)+"");
        return retVal;
    }

    /**
     * 인증 ( 데이터 확인 )
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.pollra.aop.jwt.anno.TokenCertification)")
    public Object tokenCertification(ProceedingJoinPoint pjp) throws Throwable{
        long begin = System.currentTimeMillis();
        jwtService.certification();
        Object retVal = pjp.proceed();  // 메서드 호출 자체를 감쌈
        log.info((System.currentTimeMillis() - begin)+"");
        return retVal;
    }
}
