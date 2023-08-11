package com.blograss.blograsslive.apis.auth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blograss.blograsslive.apis.auth.Object.GithubAuthTokenResponseDTO;
import com.blograss.blograsslive.apis.auth.Object.GithubUserResponseDTO;
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
    RedisTokenService redisTokenService;

    @Override
    public ResponseEntity<Message> login(String code) {
        try {
            URL url = new URL("https://github.com/login/oauth/access_token");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");

            // 이 부분에 client_id, client_secret, code를 넣어주자.
            // 여기서 사용한 secret 값은 사용 후 바로 삭제하였다.
            // 실제 서비스나 깃허브에 올릴 때 이 부분은 항상 주의하자.
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
                bw.write("client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code);
                bw.flush();
            }

            int responseCode = conn.getResponseCode();

            String responseData = getResponse(conn, responseCode);
            conn.disconnect();
            ObjectMapper mapper = new ObjectMapper();

            GithubAuthTokenResponseDTO tokenResponse = mapper.readValue(responseData, GithubAuthTokenResponseDTO.class);

            String accessToken = tokenResponse.getAccess_token();
            String refreshToken = tokenResponse.getRefresh_token();
            Integer accessExpiresIn = tokenResponse.getExpires_in();
            Integer refreshExpiresIn = tokenResponse.getRefresh_token_expires_in();

            // 2. 액세스 토큰을 사용하여 사용자 정보 가져오기
            GithubUserResponseDTO userResponse = getUserInfo(accessToken);
            String userId = userResponse.getLogin();

            redisTokenService.saveGithubToken(userId, accessToken, refreshToken, accessExpiresIn, refreshExpiresIn);

            Map<String, String> tokens = new HashMap<>();

            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return ResponseEntity.ok().body(Message.write("SUCCESS", tokens));

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));

        }

    }

    public ResponseEntity<Message> refreshAccessToken(String refreshToken, String accessToken) {
        try {

            URL userUrl = new URL("https://github.com/login/oauth/access_token");
            HttpURLConnection conn = (HttpURLConnection) userUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
                bw.write("client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=refresh_token"
                        + "&refresh_token=" + refreshToken);
                bw.flush();
            }
            
            int responseCode = conn.getResponseCode();

            String responseData = getResponse(conn, responseCode);
         
            conn.disconnect();
            ObjectMapper mapper = new ObjectMapper();

            GithubAuthTokenResponseDTO tokenResponse = mapper.readValue(responseData, GithubAuthTokenResponseDTO.class);

            String responseAccessToken = tokenResponse.getAccess_token();
            String responseRefreshToken = tokenResponse.getRefresh_token();
            Integer accessExpiresIn = tokenResponse.getExpires_in();
            Integer refreshExpiresIn = tokenResponse.getRefresh_token_expires_in();

             // 2. 액세스 토큰을 사용하여 사용자 정보 가져오기
            GithubUserResponseDTO userResponse = getUserInfo(responseAccessToken);
            String userId = userResponse.getLogin();

            redisTokenService.saveGithubToken(userId, responseAccessToken, responseRefreshToken, accessExpiresIn, refreshExpiresIn);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", responseAccessToken);
            tokens.put("refreshToken", responseRefreshToken);

            return ResponseEntity.ok().body(Message.write("SUCCESS", tokens));

        } catch (Exception e) {
            
            e.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("INTERNAL_SERVER_ERROR", e));
            
        }
    }
    
    private GithubUserResponseDTO getUserInfo(String accessToken) throws IOException {
        URL userUrl = new URL("https://api.github.com/user");
        HttpURLConnection userConn = (HttpURLConnection) userUrl.openConnection();

        userConn.setRequestMethod("GET");
        userConn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int userResponseCode = userConn.getResponseCode();

        String userResponseData = getResponse(userConn, userResponseCode);
        userConn.disconnect();

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(userResponseData, GithubUserResponseDTO.class);
    }
    
    private String getResponse(HttpURLConnection conn, int responseCode) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (responseCode == 200) {
            try (InputStream is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    sb.append(line);
                }
            }
        }
        return sb.toString();
    }
}
