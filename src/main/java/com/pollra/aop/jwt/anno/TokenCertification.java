package com.pollra.aop.jwt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 인증
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface TokenCertification {
}
