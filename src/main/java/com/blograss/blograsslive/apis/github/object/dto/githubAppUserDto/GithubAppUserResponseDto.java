package com.blograss.blograsslive.apis.github.object.dto.githubAppUserDto;

import java.util.List;

import lombok.Data;

@Data
public class GithubAppUserResponseDto {
    
    private Integer total_count;

    private List<GithubAppInfoDto> installations;

}
