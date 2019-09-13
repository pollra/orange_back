package com.pollra.web.user.domain;

import com.pollra.web.user.domain.en.UserAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserAccountDto{
    private String userId;
    private String password;
    private boolean locked;
    private UserAuth auth;

}
