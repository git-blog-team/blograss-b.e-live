package com.blograss.blograsslive.apis.repoTest.object.dto.githubRepositoryDto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GithubRepositoryMakeDTO {
    
    private String name;

    private String description;

    @JsonProperty("private")
    private Boolean private_ = false;

    private Boolean is_template = false;
}
