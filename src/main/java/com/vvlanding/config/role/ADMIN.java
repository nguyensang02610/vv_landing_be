package com.vvlanding.config.role;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.vvlanding.config.role.RoleConstant.ROLE_ADMIN;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
@PreAuthorize(ROLE_ADMIN)
public @interface ADMIN {
}
