package com.blograss.blograsslive.apis.github.object.dto.githubPutDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubPutDto {

    private String owner;

    private String repo = "blograss";

    private String path;

    private String message = "blograss posting";

    private String content;
}
