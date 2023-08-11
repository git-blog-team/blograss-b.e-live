package com.blograss.blograsslive.commons.config.GithubAuthToken;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.blograss.blograsslive.commons.utils.RedisTokenService;

public class GithubAuthProvider implements AuthenticationProvider {
    
    @Autowired
    RedisTokenService redisTokenService;

   @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getPrincipal();
        // token을 검증하고 UserDetails 정보를 로드합니다.
        String userIdFromRedis = redisTokenService.getAccessToken(token);
         
        if (userIdFromRedis == null) {
            throw new BadCredentialsException("Invalid token");
        }
        return new UsernamePasswordAuthenticationToken(userIdFromRedis, null, Collections.emptyList());
    }

   @Override
    public boolean supports(Class<?> authentication) {
        return GithubAuthToken.class.equals(authentication);
    }
}