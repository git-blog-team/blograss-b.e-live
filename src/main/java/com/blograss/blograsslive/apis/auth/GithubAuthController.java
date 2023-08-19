package com.blograss.blograsslive.apis.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blograss.blograsslive.commons.response.Message;
import com.blograss.blograsslive.commons.utils.RedisTokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class GithubAuthController {

    @Autowired
    private GithubAuthService githubAuthService;

    @Autowired
    private RedisTokenService redisTokenService;

    @GetMapping
    public ResponseEntity<Message> getCode(@RequestParam String code) throws IOException {
        if (code == null) {
            return ResponseEntity.badRequest().body(Message.write("code is null"));
        }
        
        return githubAuthService.login(code);
    }

    @GetMapping("/user")
    public ResponseEntity<Message> getUser(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        return githubAuthService.getUser(accessToken);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Message> logout(HttpServletRequest request) {
        String refreshToken = request.getHeader("RAuthorization").replace("Bearer ", "");;;
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");;

        if (refreshToken == null || accessToken == null) {
            return ResponseEntity.badRequest().body(Message.write("AccessToken or RefreshToken is null"));
        }

        return githubAuthService.logout(refreshToken,accessToken);
    }

    @PostMapping("/tokenrepubilsh")
    public ResponseEntity<Message> refreshAccessToken(HttpServletRequest request)  {
        String refreshToken = request.getHeader("RAuthorization").replace("Bearer ", "");;;
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");;

        if (refreshToken == null || accessToken == null) {
            return ResponseEntity.badRequest().body(Message.write("AccessToken or RefreshToken is null"));
        }
        
        String userRefreshToken = redisTokenService.getRefreshToken(refreshToken);

        if(userRefreshToken == null) {
            return ResponseEntity.badRequest().body(Message.write("Invalid User"));
        } else {
            redisTokenService.removeRefreshTokens(userRefreshToken);
        }

        return githubAuthService.refreshAccessToken(refreshToken,accessToken);
    }

    
}