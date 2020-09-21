package com.pollra.config.security.helper;

import com.pollra.engine.entity.Account;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PrincipalHelper {

    // TODO : 사용자 로그인 기능 업데이트 후 작성해야 함
    public static Account getAccount() {
        return new Account();
    }

    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!ObjectUtils.allNotNull(authentication)
                || BooleanUtils.isFalse(authentication.isAuthenticated())
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return authentication;
    }
}
