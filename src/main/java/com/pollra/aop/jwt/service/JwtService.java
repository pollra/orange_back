package com.pollra.aop.jwt.service;

import com.pollra.aop.jwt.config.JwtConstants;
import com.pollra.aop.jwt.config.Range;
import com.pollra.aop.jwt.exception.*;
import com.pollra.web.repository.UserAccountRepository;
import com.pollra.web.user.domain.UserAccount;
import com.pollra.web.user.tool.data.UserDataPretreatmentTool;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class JwtService {
    private UserAccountRepository repository;
    private HttpServletRequest request;
    private PasswordEncoder passwordEncoder;
    private JwtDataTool tool;
    private UserDataPretreatmentTool userTool;

    public JwtService(UserAccountRepository repository, HttpServletRequest request, PasswordEncoder passwordEncoder, JwtDataTool tool, UserDataPretreatmentTool userTool) {
        this.repository = repository;
        this.request = request;
        this.passwordEncoder = passwordEncoder;
        this.tool = tool;
        this.userTool = userTool;
    }

    /**
     * 인증
     *
     * @throws ExpiredJwtException
     * @throws UnsupportedJwtException
     * @throws MalformedJwtException
     * @throws SignatureException
     * @throws IllegalArgumentException
     * @throws JwtServiceException
     */
    public void certification() throws ExpiredJwtException,
            UnsupportedJwtException,
            MalformedJwtException,
            SignatureException,
            IllegalArgumentException,
            JwtDataAccessException,
            JwtTamperingDetectionException{
        log.info("certification start");
        var token = request.getHeader(JwtConstants.TOKEN_HEADER);
        // 저장한 토큰 데이터가 null 인가?
        if(token == null){
            log.error("토큰 데이터를 찾을 수 없습니다.");
            throw new IllegalArgumentException();
        }
        JwtParsing jwtParsing = new JwtParsing(token).invoke();
        String username = jwtParsing.getUsername();
        String authorities = jwtParsing.getAuthorities();

        log.warn("certification username: {}", username);
        log.warn("certification jwtParsing.getUsername: {}", jwtParsing.getUsername());
//            log.warn("입력된 유저 권한: {}",parsedToken.getBody().get("rol"));

        // 유저의 권한이 맞는지 확인
        UserAccount userAccount = null;
        try {
            userAccount = repository.getById(username);
        } catch (Exception e) {
            log.warn("[!] Jwt 존재하지 않는 유저 엑세스 감지. [user: {}]", username);
            throw new JwtDataAccessException("토큰에서 넘어온 유저 데이터가 DB에 존재하지 않습니다.");
        }
        if (!(userAccount.getAuth().equals(authorities))) {
            log.warn("[!] Jwt 유저 권한 변조 감지. [user: {}, auth: {}, 변조하려는 권한: {}]", username, userAccount.getAuth(), authorities);
            throw new JwtTamperingDetectionException("토큰데이터 변조가 감지되었습니다.");
        }
        request.setAttribute("jwt-user", username);
        request.setAttribute("jwt-auth", userAccount.getAuth());
    }

    // 인가
    public void credential() throws Throwable{

        // 토큰 데이터가 이미 존재하면 예외 발동
        String header = request.getHeader(JwtConstants.TOKEN_HEADER);
        // 저장한 토큰 데이터가 null 인가?
        if (!StringUtils.isEmpty(header) || header.startsWith(JwtConstants.TOKEN_PREFIX)) {
            throw new JwtFindException("이미 로그인 되어있습니다.");
        }

        // 데이터 존재여부, 가입데이터, 비밀번호 일치 여부 를 순서대로 확인 후 DB 에서 가져온 데이터를 리턴.
        UserAccount user = checkUserData();
        log.warn("user: {}", user.toString());

        // Jwt 를 만들어 token 에 저장
        String token = createJwt(user);

        // request 에 Authorization 이름으로 토큰값 저장 후 컨트롤러에서 사용할 수 있게 함.
        request.setAttribute(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX+token);
        request.setAttribute("loginUser", user.getId());
    }

    /**
     * 로그아웃
     *
     * @throws Throwable            : 예상하지 못한 에러
     * @throws JwtNotFoundException : 토큰이 존재하지 않음
     * @throws JwtFindException     : 변조된 토큰 요청
     */
    public void tokenLogout() throws Throwable{
        String token = request.getHeader(JwtConstants.TOKEN_HEADER);
        JwtParsing jwtParsing = new JwtParsing(token).invoke();
        if(StringUtils.isEmpty(token)){
            throw new JwtNotFoundException("로그아웃 할 토큰이 존재하지 않습니다.");
        }
        UserAccount userAccount = userTool.getUserAccount(com.pollra.web.user.domain.en.Range.ID);
        if(!jwtParsing.username.equals(userAccount.getId())){
            throw new JwtFindException("토큰 데이터와 일치하지 않는 사용자의 요청입니다.");
        }
//        JwtParsing jwtParsing = new JwtParsing(token).invoke();
        request.setAttribute(JwtConstants.TOKEN_HEADER, "");
    }

    private UserAccount checkUserData() throws JwtServiceException{
        UserAccount user = tool.getUserAccount();
        if(StringUtils.isEmpty(user.getId())){
            throw new JwtDataAccessException("데이터를 확인할 수 없습니다.");
        }

        // user 의 auth 를 DB 에서 가져온다.
        UserAccount dbAccessUser = repository.getById(user.getId());
        if(tool.isNull(Range.ID_PW_AUTH, dbAccessUser)){
            throw new JwtDataAccessException("가입 데이터를 확인할 수 없습니다.");
        }
        // 유저의 패스워드가 일치하는지 확인
        if(!passwordEncoder.matches(user.getPassword(), dbAccessUser.getPassword())){
            throw new JwtDataAccessException("비밀번호가 일치하지 않습니다.");
        }
        return dbAccessUser;
    }

    private String createJwt(UserAccount user) throws IOException{
        String roles = user.getAuth();

        byte[] signingKey = JwtConstants.JWT_SECRET.getBytes();
        log.warn("토큰 만료시간: {}", new SimpleDateFormat("yyyy.MM.dd(kk:mm-ss)").format(new Date(System.currentTimeMillis()+(1000*60*30))));
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ",JwtConstants.TOKEN_TYPE)
                .setIssuer(JwtConstants.TOKEN_ISSUER)
                .setAudience(JwtConstants.TOKEN_AUDIENCE)
                .setSubject(user.getId())
                .setExpiration(new Date(System.currentTimeMillis()+(1000*60*30)))
                .claim("rol",roles)
                .compact();
    }

    private class JwtParsing {
        private String token;
        private String username;
        private String authorities;

        public JwtParsing(String token) {
            this.token = token;
        }

        public String getUsername() {
            return username;
        }

        public String getAuthorities() {
            return authorities;
        }

        /*private void setAuthorities(Map<String, String> map){
            List<String> result = new ArrayList<>();
            for(Map.Entry entry : map.entrySet()){
                result.add(entry.getValue()+"");
            }
            this.authorities = result;
        }*/

        public JwtParsing invoke() {
            if (StringUtils.isEmpty(token) && !token.startsWith(JwtConstants.TOKEN_PREFIX)) {
                throw new JwtNotFoundException("토큰 데이터가 존재하지 않습니다.");
            }
            var signingKey = JwtConstants.JWT_SECRET.getBytes();

            var parsedToken = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token.replace("Bearer", ""));
            log.info("parsedToken: " + parsedToken);
            username = parsedToken
                    .getBody()
                    .getSubject();
            log.info("username: " + username);
            log.warn("권한: {}",parsedToken.getBody());
            log.warn("권한.get(): {}",parsedToken.getBody().get("rol"));
            this.authorities = parsedToken.getBody().get("rol")+"";
//            this.setAuthorities((Map<String, String>) parsedToken.getBody().get("rol"));
//            authorities = ((List<String>) parsedToken.getBody().get("rol"));
            return this;
        }
    }
}
