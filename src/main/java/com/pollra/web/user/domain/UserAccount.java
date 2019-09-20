package com.pollra.web.user.domain;

import com.pollra.web.user.domain.en.UserAuth;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@ToString
@Table(name = "user_account")
public class UserAccount {
    @Id
    @GeneratedValue
    private Long num;

    @NotBlank(message = "아이디를 작성해주세요")
    @Column(unique = true)
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "비밀번호 확인을 작성해주세요")
    @Column(nullable = false, name = "password_match")
    private String passwordMatch;

    @Column(nullable = false)
    private String auth;

    @Email(message = "메일 양식을 지켜주세요")
    private String email;

    private boolean locked = false;

}
