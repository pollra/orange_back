package com.pollra.web.user.domain;

import com.pollra.web.user.domain.en.UserAuth;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "user_account")
public class UserAccount {
    @Id
    @GeneratedValue
    private Long num;

    @Column(unique = true)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "password_match")
    private String passwordMatch;

    @Column(nullable = false)
    private String auth;

    private String email;

    private boolean locked = false;

}
