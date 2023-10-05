package com.blograss.blograsslive.apis.github.object.dto.githubGetContentsDto;

import lombok.Data;

@Data
public class GithubGetContentsResponseDto {
    
    private String name;
    
    private String path;

    private String sha;
}
