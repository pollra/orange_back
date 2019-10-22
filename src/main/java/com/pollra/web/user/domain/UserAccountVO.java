package com.pollra.web.user.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@NotEmpty
@Getter
@ToString
public class UserAccountVO {
    private Long num;
    private String id;
    private String email;
    private boolean locked;
    private String auth;

    public UserAccountVO(){}

    public UserAccountVO(Long num, String id, String email, boolean locked, String auth) {
        this.num = num;
        this.id = id;
        this.email = email;
        this.locked = locked;
        this.auth = auth;
    }

    public UserAccountVO(UserAccount userAccount){
        this.num = userAccount.getNum();
        this.id = userAccount.getId();
        this.email = userAccount.getEmail();
        this.locked = userAccount.isLocked();
        this.auth = userAccount.getAuth();
    }
}
