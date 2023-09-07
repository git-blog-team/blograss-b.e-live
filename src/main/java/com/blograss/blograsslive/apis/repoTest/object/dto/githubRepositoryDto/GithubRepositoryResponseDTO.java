package com.blograss.blograsslive.apis.repoTest.object.dto.githubRepositoryDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepositoryResponseDTO {
    
    private String name;

    private String description;

}
