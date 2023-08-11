package com.blograss.blograsslive.apis.auth.object.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubUserResponseDTO {
    
    private String login;
    
    private String id;

    private String name;
}
