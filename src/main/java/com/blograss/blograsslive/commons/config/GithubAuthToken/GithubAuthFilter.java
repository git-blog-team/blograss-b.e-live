package com.blograss.blograsslive.commons.config.GithubAuthToken;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blograss.blograsslive.commons.utils.RedisTokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GithubAuthFilter extends OncePerRequestFilter {
    
    @Autowired
    RedisTokenService redisTokenService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                String bearerToken = request.getHeader("Authorization");
                System.out.print(bearerToken);

                if (bearerToken != null && authenticateWithToken(bearerToken)) {

                    GithubAuthToken githubAuthToken = new GithubAuthToken(bearerToken);
                    githubAuthToken.setAuthenticated(true);  // 여기에서 인증 상태를 true로 변경
                    SecurityContextHolder.getContext().setAuthentication(githubAuthToken);
                }

                filterChain.doFilter(request, response);
    }

    private boolean authenticateWithToken(String bearerToken) {

        String token = bearerToken.replace("Bearer ", "");
        String redisToken = redisTokenService.getAccessToken(token);

        return redisToken != null && !redisToken.isEmpty();
    }
}