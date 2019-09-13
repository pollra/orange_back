package com.pollra.filter.jwt;

import com.pollra.config.security.SecurityConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 모든 HTTP 요청을 처리하고
 * 올바른 토큰을 가진 Authorization 헤더가 있는지 확인합니다.
 *  (예를들어 토큰이 만료되지 않았거나 서명 키가 올바른 경우)
 * 토큰이 유효하면 필터는 Spring 의 보안 컨텍스트에 인증 데이터를 추가합니다.
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        var authentication = getAuthentication(request);
        if(authentication == null){
            chain.doFilter(request, response);
            return;
        }

        log.info("JwtAuthorizationFilter start");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

//        log.info("SecurityContextHolder["+
//                "Principal: "+SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString() +", "+
//                "Authorities: "+SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString() +"] "
//        );
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        var token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if(!StringUtils.isEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)){
            try {
                var signingKey = SecurityConstants.JWT_SECRET.getBytes();

        log.info("ㅇㅅㅇ???????");
                var parsedToken = Jwts.parser()
                        .setSigningKey(signingKey)
                        .parseClaimsJws(token.replace("Bearer",""));
                var username = parsedToken
                        .getBody()
                        .getSubject();
                var authorities = ((List<?>) parsedToken.getBody()
                        .get("rol")).stream()
                        .map(authority -> new SimpleGrantedAuthority((String)authority))
                        .collect(Collectors.toList());
                if(!StringUtils.isEmpty(username)){
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
            }catch (ExpiredJwtException exception){
                log.warn("만료된 JWT 구문 분석 요청 : {} failed : {}", token, exception.getMessage());
            }catch (UnsupportedJwtException exception){
                log.warn("지원되지 않는 JWT 구문 분석 요청 : {} failed : {}", token, exception.getMessage());
            }catch (MalformedJwtException exception){
                log.warn("유효하지 않은 JWT 구문 분석 요청 : {} failed : {}", token, exception.getMessage());
            }catch (SignatureException exception) {
                log.warn("유효하지 않은 서명으로 구문 분석 JWT 요청 : {} failed : {}", token, exception.getMessage());
            }catch (IllegalArgumentException exception){
                log.warn("비어 있거나 널인 JWT 구문 분석 요청 : {} failed : {}", token, exception.getMessage());
            }
        }
        return null;
    }
}

