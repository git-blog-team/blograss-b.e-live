package com.blograss.blograsslive.apis.github.object.dto.githubPutDto;

import lombok.Data;

@Data
public class GithubPutResponseCommitDto {

    private String message;

    private GithubPutResponseCommitter committer;
    
}
