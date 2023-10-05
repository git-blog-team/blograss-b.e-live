package com.blograss.blograsslive.apis.github.object.dto.githubPutDto;

import lombok.Data;

@Data
public class GithubPutResponseDto {

    private GithubPutResponseContentDto content;

    private GithubPutResponseCommitDto commit;
    
}
