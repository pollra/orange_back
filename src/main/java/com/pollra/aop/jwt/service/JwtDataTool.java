package com.pollra.aop.jwt.service;

import com.pollra.aop.jwt.config.Range;
import com.pollra.aop.jwt.exception.JwtDataAccessException;
import com.pollra.aop.jwt.exception.JwtToolException;
import com.pollra.web.user.domain.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class JwtDataTool {
    private final String USERNAME = "username";
    private final String PASSWORD = "password";

    private HttpServletRequest request;

    public JwtDataTool(HttpServletRequest request) {
        this.request = request;
    }

    public UserAccount getUserAccount(){
        UserAccount userAccount = new UserAccount();
        userAccount.setId(get(USERNAME));
        userAccount.setPassword(get(PASSWORD));
        return userAccount;
    }

    private String get(String target){
        return request.getParameter(target);
    }

    public boolean isNull(Range range,UserAccount userAccount){
        switch (range){
            case ID:
                return isNull_id(userAccount);
            case PW:
                return isNull_pw(userAccount);
            case ID_PW:
                return isNull_id_pw(userAccount);
            case ID_PW_AUTH:
                return isNull_id_pw_auth(userAccount);
            default:
                throw new JwtToolException("JwtDataTool : 선택된 항목은 존재하지 않는 항목입니다.");
        }
    }

    private boolean isNull_id(UserAccount userAccount){
        if(StringUtils.isEmpty(userAccount.getId())) return true;
        return false;
    }

    private boolean isNull_pw(UserAccount userAccount){
        if(StringUtils.isEmpty(userAccount.getPassword())) return true;
        if(StringUtils.isEmpty(userAccount.getPasswordMatch())) return true;
        return false;
    }

    private boolean isNull_id_pw(UserAccount userAccount){
        if(StringUtils.isEmpty(userAccount.getId())) return true;
        if(StringUtils.isEmpty(userAccount.getPassword())) return true;
        return false;
    }

    private boolean isNull_id_pw_auth(UserAccount userAccount){
        if(StringUtils.isEmpty(userAccount.getId())) return true;
        if(StringUtils.isEmpty(userAccount.getPassword())) return true;
        if(StringUtils.isEmpty(userAccount.getAuth())) return true;
        return false;
    }
}
