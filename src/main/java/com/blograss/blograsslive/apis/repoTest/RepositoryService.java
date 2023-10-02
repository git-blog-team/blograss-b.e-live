package com.blograss.blograsslive.apis.repoTest;

import org.springframework.http.ResponseEntity;

import com.blograss.blograsslive.apis.repoTest.object.dto.githubPutDto.GithubPutDto;
import com.blograss.blograsslive.apis.repoTest.object.dto.githubRepositoryDto.GithubRepositoryMakeDTO;
import com.blograss.blograsslive.commons.response.Message;

public interface RepositoryService {

    ResponseEntity<Message> CreateRepo(GithubRepositoryMakeDTO RepoMakeDTO, String accessToken);

    ResponseEntity<Message> PutGit(GithubPutDto githubPutDto, String accessToken);

}