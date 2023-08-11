package com.blograss.blograsslive.commons.config.GithubAuthToken;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class GithubAuthToken implements Authentication {

    private String token;
    private boolean isAuthenticated;

    public GithubAuthToken(String token) {
        this.token = token;
        this.isAuthenticated = false; // 기본적으로 인증되지 않은 상태로 설정
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 필요에 따라 권한 목록 반환
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return token; // 여기서는 token을 반환하지만, 필요에 따라 다르게 설정 가능
    }

    @Override
    public Object getDetails() {
        return null; // 세부 정보 필요 시 설정
    }

    @Override
    public Object getPrincipal() {
        return null; // 주요 주체 필요 시 설정
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return null; // 이름 필요 시 설정
    }
}