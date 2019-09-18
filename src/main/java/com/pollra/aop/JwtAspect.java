package com.pollra.aop;

import com.pollra.aop.jwt.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class JwtAspect {

    private JwtService jwtService;
    private HttpServletRequest request;

    public JwtAspect(JwtService jwtService, HttpServletRequest request) {
        this.jwtService = jwtService;
        this.request = request;
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
     * @return
     * @throws Throwable
     */
    @Before("@annotation(com.pollra.aop.jwt.anno.TokenCredential)")
    public void tokenCredential(JoinPoint jp) throws Throwable{
        try {
            jwtService.credential();
        }catch (Throwable e){
            request.setAttribute("error",e.getMessage());
        }
    }

    /**
     * 인증 ( 데이터 확인 )
     *
     * @return
     * @throws Throwable
     */
    @Before("@annotation(com.pollra.aop.jwt.anno.TokenCertification)")
    public void tokenCertification(JoinPoint jp) throws Throwable{
        try {
            jwtService.certification();
        }catch (Throwable e){
            request.setAttribute("error",e.getMessage());
        }
    }
}
