package com.blograss.blograsslive.apis.github.object.dto.githubDeleteDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubDeleteContentsDto {

    private String message = "Delete blograss";

    private String sha;

}
