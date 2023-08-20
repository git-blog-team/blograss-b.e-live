package com.blograss.blograsslive.apis.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.blograss.blograsslive.apis.auth.object.User;
import com.blograss.blograsslive.apis.auth.object.dto.GithubAuthTokenResponseDTO;
import com.blograss.blograsslive.apis.auth.object.dto.GithubUserResponseDTO;
import com.blograss.blograsslive.commons.response.Message;
import com.blograss.blograsslive.commons.utils.RedisTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GithubAuthServiceImpl implements GithubAuthService {

    @Value("${spring.security.oauth2.client.registration.github.clientId}")
    private String clientId;
    
    @Value("${spring.security.oauth2.client.registration.github.clientSecret}")
    private String clientSecret;

    @Autowired
    private RedisTokenService redisTokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Message> login(String code) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "https://github.com/login/oauth/access_token";

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "application/json");
            headers.add("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");

            // Set body
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("code", code);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            // Make the POST request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // Get the response body
            String responseData = response.getBody();
            ObjectMapper mapper = new ObjectMapper();

            GithubAuthTokenResponseDTO tokenResponse = mapper.readValue(responseData, GithubAuthTokenResponseDTO.class);

            String accessToken = tokenResponse.getAccess_token();
            String refreshToken = tokenResponse.getRefresh_token();
            Integer accessExpiresIn = tokenResponse.getExpires_in();
            Integer refreshExpiresIn = tokenResponse.getRefresh_token_expires_in();

            // 2. 액세스 토큰을 사용하여 사용자 정보 가져오기
            GithubUserResponseDTO userResponse = getUserInfo(accessToken);
            String userId = userResponse.getLogin();
            String userName = userResponse.getName();

            redisTokenService.saveGithubToken(userId, accessToken, refreshToken, accessExpiresIn, refreshExpiresIn);

            Map<String, String> tokens = new HashMap<>();

            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            if (userRepository.findByUserId(userId) == null) {
                User user = new User();
                user.setUserId(userId);
                user.setUserName(userName);
                userRepository.save(user);
            }

            return ResponseEntity.ok().body(Message.write("SUCCESS", tokens));

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));

        }

    }

    public ResponseEntity<Message> getUser(String accessToken) {
        try {
            String userId = redisTokenService.getUserId(accessToken);
            User user = userRepository.findByUserId(userId);
            return ResponseEntity.ok().body(Message.write("SUCCESS", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }
    
    public ResponseEntity<Message> logout(String refreshToken, String accessToken) {
        try {
            redisTokenService.removeTokens(accessToken, refreshToken);
            return ResponseEntity.ok().body(Message.write("SUCCESS", "Logout Success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    public ResponseEntity<Message> refreshAccessToken(String refreshToken, String accessToken) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            String url = "https://github.com/login/oauth/access_token";

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "application/json");
            // Set body
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("grant_type", "refresh_token");
            body.add("refresh_token", refreshToken);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
            // Make the POST request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            // Get the response body
            String responseData = response.getBody();
            ObjectMapper mapper = new ObjectMapper();

            GithubAuthTokenResponseDTO tokenResponse = mapper.readValue(responseData, GithubAuthTokenResponseDTO.class);

            String responseAccessToken = tokenResponse.getAccess_token();
            String responseRefreshToken = tokenResponse.getRefresh_token();
            Integer accessExpiresIn = tokenResponse.getExpires_in();
            Integer refreshExpiresIn = tokenResponse.getRefresh_token_expires_in();

            // 2. 액세스 토큰을 사용하여 사용자 정보 가져오기
            GithubUserResponseDTO userResponse = getUserInfo(responseAccessToken);
            String userId = userResponse.getLogin();

            redisTokenService.saveGithubToken(userId, responseAccessToken, responseRefreshToken, accessExpiresIn,
                    refreshExpiresIn);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", responseAccessToken);
            tokens.put("refreshToken", responseRefreshToken);

            return ResponseEntity.ok().body(Message.write("SUCCESS", tokens));

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));

        }
    }
    
    public ResponseEntity<Message> blogUserNameUpdate(String userId, String blogUserName) {
        try {

            User user = userRepository.findByUserId(userId);
            user.setBlogUserName(blogUserName);
            userRepository.save(user);
            return ResponseEntity.ok().body(Message.write("SUCCESS", user));

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));

        }
    }
    
    
    private GithubUserResponseDTO getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.github.com/user";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        // Make the GET request
        ResponseEntity<GithubUserResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, GithubUserResponseDTO.class);

        return response.getBody();
    }
    
}
