package com.blograss.blograsslive.apis.repoTest.object.dto.githubPutDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubPutDto {

    private String owner;

    private String repo;

    private String path;

    private String message;

    private String content;
}
