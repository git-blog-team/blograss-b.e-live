package com.blograss.blograsslive.apis.github.object.dto.githubRepositoryDto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GithubRepositoryMakeDTO {
    
    private String name = "blograss";

    private String description = "blograss-repository";

    @JsonProperty("private")
    private Boolean private_ = false;

    private Boolean is_template = false;
}
