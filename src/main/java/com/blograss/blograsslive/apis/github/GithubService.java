package com.blograss.blograsslive.apis.github;

import java.util.List;

import com.blograss.blograsslive.apis.github.object.dto.githubGetContentsDto.GithubGetContentsResponseDto;
import com.blograss.blograsslive.apis.github.object.dto.githubPutDto.GithubPutDto;
import com.blograss.blograsslive.apis.github.object.dto.githubPutDto.GithubPutResponseDto;
import com.blograss.blograsslive.apis.github.object.dto.githubRepositoryDto.GithubRepositoryResponseDTO;

public interface GithubService {

    GithubRepositoryResponseDTO createRepo(String accessToken);

    GithubPutResponseDto putGit(GithubPutDto githubPutDto, String accessToken);

    GithubGetContentsResponseDto getRepoContent(GithubPutDto githubPutDto, String accessToken);

    List<GithubGetContentsResponseDto> getRepoContents(GithubPutDto githubPutDto, String accessToken);

    GithubRepositoryResponseDTO getRepo(String owner, String accessToken);

    String deleteContents(GithubPutDto githubPutDto, String accessToken, String sha);

    Boolean validInstallGithubApp(String accessToken);

}