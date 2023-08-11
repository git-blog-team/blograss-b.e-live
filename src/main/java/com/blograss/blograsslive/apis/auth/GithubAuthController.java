package com.blograss.blograsslive.apis.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blograss.blograsslive.commons.response.Message;
import com.blograss.blograsslive.commons.utils.RedisTokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class GithubAuthController {

    @Autowired
    GithubAuthService githubAuthService;

    @Autowired
    RedisTokenService redisTokenService;

    @GetMapping("/auth")
    public ResponseEntity<Message> getCode(@RequestParam String code) throws IOException {
        if (code == null) {
            return ResponseEntity.badRequest().body(Message.write("code is null"));
        }
        return githubAuthService.login(code);
    }

    @PostMapping("/auth/tokenrepubilsh")
    public ResponseEntity<Message> refreshAccessToken(HttpServletRequest request)  {
        String refreshToken = request.getHeader("RAuthorization");
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");;

        if (refreshToken == null || accessToken == null) {
            return ResponseEntity.badRequest().body(Message.write("AccessToken or RefreshToken is null"));
        }
        
        String userRefreshToken = redisTokenService.getRefreshToken(refreshToken);

        if(userRefreshToken == null) {
            return ResponseEntity.badRequest().body(Message.write("Invalid User"));
        } else {
            redisTokenService.removeRefreshTokens(refreshToken);
        }

        return githubAuthService.refreshAccessToken(refreshToken,accessToken);
    }

    
}