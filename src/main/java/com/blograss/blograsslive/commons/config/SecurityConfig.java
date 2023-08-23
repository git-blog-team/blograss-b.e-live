package com.blograss.blograsslive.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.blograss.blograsslive.commons.config.GithubAuthToken.GithubAuthDeniedHandler;
import com.blograss.blograsslive.commons.config.GithubAuthToken.GithubAuthEntryPoint;
import com.blograss.blograsslive.commons.config.GithubAuthToken.GithubAuthFilter;
import com.blograss.blograsslive.commons.config.GithubAuthToken.GithubAuthProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    


    @Autowired
    private GithubAuthEntryPoint githubAuthEntryPoint;

    @Autowired
    private GithubAuthDeniedHandler githubAuthDeniedHandler;

    @Autowired
    private GithubAuthFilter githubAuthFilter;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .cors()
            .configurationSource(corsConfigurationSource())
        .and()
            .exceptionHandling()
			.authenticationEntryPoint(githubAuthEntryPoint)
			.accessDeniedHandler(githubAuthDeniedHandler)
        .and()
            .httpBasic()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeHttpRequests()
            .requestMatchers(
                "/",
                "/auth", 
                "/login/github", 
                "/login", 
                "/auth/tokenrepubilsh", 
                "/post/list"
            )
            .permitAll()
            .requestMatchers(HttpMethod.GET, "/post")
            .permitAll()
            .requestMatchers(HttpMethod.GET, "/comment")
            .permitAll()
            .anyRequest()
            .authenticated()
        .and()
            .addFilterBefore(githubAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .authenticationProvider(new GithubAuthProvider()).oauth2Login().authorizationEndpoint().baseUri("/login");
            
        return http.build();
    }
    
    
    @Bean
	public CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true); // 서버가 응답시 json을 자바스크립트에서 처리할 수 있게 할지 설정
		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
