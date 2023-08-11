package com.blograss.blograsslive.apis.auth;

import org.springframework.http.ResponseEntity;

import com.blograss.blograsslive.commons.response.Message;

public interface GithubAuthService {

    ResponseEntity<Message> login(String code);
    
    ResponseEntity<Message> refreshAccessToken(String refreshToken, String accessToken);
}