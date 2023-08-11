package com.blograss.blograsslive.apis.auth.Object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubUserResponseDTO {
    
    private String login;
    
    private String id;

}
