package com.blograss.blograsslive.apis.repoTest.object.dto.githubPutDto;

import lombok.Data;

@Data
public class GithubPutResponseDto {

    private GithubPutResponseContentDto content;

    private GithubPutResponseCommitDto commit;
    
}
