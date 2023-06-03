package com.vvlanding.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableJpaAuditing
public class WhiteListIP implements AuthenticationProvider {
    Set<String> whitelist = new HashSet<String>();

    public WhiteListIP() {
        //-- getALL IP
        whitelist.add("103.237.145.183");
        whitelist.add("14.177.142.58");
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
        String userIp = details.getRemoteAddress();
        if (!whitelist.contains(userIp)) {
            throw new BadCredentialsException("Invalid IP Address");
        }
        return auth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
