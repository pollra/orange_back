package com.pollra.filter.jwt;

import com.pollra.config.security.SecurityConstants;
import com.pollra.tool.http.InfoPrint;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;


/**
 * 사용자 인증에 직접 사용.
 * URL 에서 사용자 이름 및 비밀번호 매개 변수를 확인하고 Spring 의
 * 인증 관리자를 호출하여 확인합니다.
 *
 * 사용자 이름과 비밀번호가 올바르면 필터가 JWT 토큰을 작성하여 HTTP
 * 권한 헤더에 리턴합니다.
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        log.info("JwtAuthenticationFilter start");
        var username = request.getParameter("username");
        var password = request.getParameter("password");

        /**
         * UsernamePasswordAuthenticationToken
         * 사용자의 이름(UserName) 과 비밀번호를 간단하게 표시하는 클래스
         */
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        var user = ((User) authResult.getPrincipal());

        var roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var signingKey = SecurityConstants.JWT_SECRET.getBytes();

        var token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ",SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()+864000000))
                .claim("rol",roles)
                .compact();
        log.info(token);
//        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        response.getWriter().print(
                "{\""+SecurityConstants.TOKEN_HEADER+"\":\""+SecurityConstants.TOKEN_PREFIX + token+"\"}");
        for(String a : response.getHeaderNames()){
            log.info(a +" : "+response.getHeader(a));
        }
        log.info("ㅇㅅㅇ???"+response.getHeader(SecurityConstants.TOKEN_HEADER));
        InfoPrint.headersPrint(request);
    }
}