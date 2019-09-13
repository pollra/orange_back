package com.pollra.web.auth.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NotEmpty
public class Auth {

    private Long num;
}
