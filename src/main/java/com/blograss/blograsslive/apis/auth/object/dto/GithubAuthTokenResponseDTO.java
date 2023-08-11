package com.blograss.blograsslive.apis.auth.object.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubAuthTokenResponseDTO {
    
    private String access_token;

    private String refresh_token;

    private int expires_in;

    private int refresh_token_expires_in;

}
