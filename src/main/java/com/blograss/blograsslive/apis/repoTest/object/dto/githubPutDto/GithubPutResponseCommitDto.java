package com.blograss.blograsslive.apis.repoTest.object.dto.githubPutDto;

import lombok.Data;

@Data
public class GithubPutResponseCommitDto {

    private String message;

    private GithubPutResponseCommitter committer;
    
}
