package com.pollra.aop;

import com.pollra.aop.jwt.exception.JwtServiceException;
import com.pollra.aop.jwt.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.SignatureException;

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
     * @throws ExpiredJwtException
     * @throws UnsupportedJwtException
     * @throws MalformedJwtException
     * @throws SignatureException
     * @throws IllegalArgumentException
     * @throws JwtServiceException
     */
    @Before("@annotation(com.pollra.aop.jwt.anno.TokenCredential)")
    public void tokenCredential(JoinPoint jp) throws Throwable{
        try {
            jwtService.credential();
        }catch (ExpiredJwtException e){
            request.setAttribute("error",e.getMessage());
        }catch (UnsupportedJwtException e){
            request.setAttribute("error",e.getMessage());
        }catch (MalformedJwtException e){
            request.setAttribute("error",e.getMessage());
        }catch (SignatureException e){
            request.setAttribute("error",e.getMessage());
        }catch (IllegalArgumentException e){
            request.setAttribute("error",e.getMessage());
        }catch (JwtServiceException e){
            request.setAttribute("error",e.getMessage());
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
        }catch (ExpiredJwtException exception){
            log.error("만료된 JWT 구문 분석 요청 failed : {}", exception.getMessage());
            request.setAttribute("error","만료된 JWT 구문 분석 요청");
        }catch (UnsupportedJwtException exception){
            log.error("지원되지 않는 JWT 구문 분석 요청 failed : {}", exception.getMessage());
            request.setAttribute("error","지원되지 않는 JWT 구문 분석 요청");
        }catch (MalformedJwtException exception){
            log.error("유효하지 않은 JWT 구문 분석 요청 failed : {}", exception.getMessage());
            request.setAttribute("error","유효하지 않은 JWT 구문 분석 요청");
        }catch (io.jsonwebtoken.security.SignatureException exception) {
            log.error("유효하지 않은 서명으로 구문 분석 JWT 요청 failed : {}",  exception.getMessage());
            request.setAttribute("error","유효하지 않은 서명으로 구문 분석 JWT 요청");
        }catch (IllegalArgumentException exception){
            log.error("비어 있거나 널인 JWT 구문 분석 요청 failed : {}", exception.getMessage());
            request.setAttribute("error","비어 있거나 널인 JWT 구문 분석 요청");
        }catch (JwtServiceException e){
            log.error(e.getMessage());
            request.setAttribute("error","인증과정에서 알 수 없는 에러가 발생했습니다");
        }catch (Throwable e){
            log.error("[!] 예상하지 못한 문제 발생: {}",e.getMessage());
            request.setAttribute("error","인증과정에서 예상하지 못한 에러가 발생했습니다.");
        }
    }

    /**
     * ADMIN 인증 ( admin 데이터 확인 )
     *
     * @return
     * @throws Throwable
     */
    @Before("@annotation(com.pollra.aop.jwt.anno.AdminCertification)")
    public void adminTokenCertification(JoinPoint jp) throws Throwable{
        try {
            jwtService.certification();
            if(!request.getAttribute("jwt-auth").toString().equals("ADMIN")){
                request.setAttribute("error","API 접근 권한이 없습니다.");
            }
        }catch (ExpiredJwtException exception){
            log.error("만료된 JWT 구문 분석 요청 failed : {}", exception.getMessage());
            request.setAttribute("error","만료된 JWT 구문 분석 요청");
        }catch (UnsupportedJwtException exception){
            log.error("지원되지 않는 JWT 구문 분석 요청 failed : {}", exception.getMessage());
            request.setAttribute("error","지원되지 않는 JWT 구문 분석 요청");
        }catch (MalformedJwtException exception){
            log.error("유효하지 않은 JWT 구문 분석 요청 failed : {}", exception.getMessage());
            request.setAttribute("error","유효하지 않은 JWT 구문 분석 요청");
        }catch (io.jsonwebtoken.security.SignatureException exception) {
            log.error("유효하지 않은 서명으로 구문 분석 JWT 요청 failed : {}",  exception.getMessage());
            request.setAttribute("error","유효하지 않은 서명으로 구문 분석 JWT 요청");
        }catch (IllegalArgumentException exception){
            log.error("비어 있거나 널인 JWT 구문 분석 요청 failed : {}", exception.getMessage());
            request.setAttribute("error","비어 있거나 널인 JWT 구문 분석 요청");
        }catch (JwtServiceException e){
            log.error(e.getMessage());
            request.setAttribute("error","인증과정에서 예상하지 못한 에러가 발생했습니다");
        }catch (Throwable e){
            log.error("[!] 예상하지 못한 문제 발생: {}",e.getMessage());
            request.setAttribute("error","인증과정에서 예상하지 못한 에러가 발생했습니다.");
        }
    }

    /**
     * 로그아웃
     *
     * @param jp
     * @throws Throwable
     */
    @Before("@annotation(com.pollra.aop.jwt.anno.TokenLogout)")
    public void tokenLogout(JoinPoint jp) throws Throwable{
        try{
            jwtService.tokenLogout();
        }catch (Throwable e){
            log.info("로그아웃 과정 에러발생");
            request.setAttribute("error",e.getMessage());
        }
    }
}
