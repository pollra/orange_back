package com.pollra.web.user.domain.en;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

public enum UserAuth implements GrantedAuthority {
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    ORANGE("ORANGE"),
    USER("USER"),
    GUEST("GUEST");

    final private String name;

    public String getName(){
        return name;
    }

    private UserAuth(String name){
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return null;
    }

    public Collection<UserAuth> getCollection(){
        return Arrays.asList(UserAuth.values());
    }
}
