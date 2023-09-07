package com.blograss.blograsslive.apis.repoTest;

import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.blograss.blograsslive.apis.repoTest.object.dto.githubPutDto.GithubPutDto;
import com.blograss.blograsslive.apis.repoTest.object.dto.githubPutDto.GithubPutResponseDto;
import com.blograss.blograsslive.apis.repoTest.object.dto.githubRepositoryDto.GithubRepositoryMakeDTO;
import com.blograss.blograsslive.apis.repoTest.object.dto.githubRepositoryDto.GithubRepositoryResponseDTO;
import com.blograss.blograsslive.commons.response.Message;

@Service
public class RepositoryServiceImpl implements RepositoryService {
    
    public ResponseEntity<Message> CreateRepo(GithubRepositoryMakeDTO RepoMakeDTO, String accessToken) {
        try {

            GithubRepositoryResponseDTO response = getParameter(RepoMakeDTO, accessToken);

            if (response == null) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR"));
            }

            return ResponseEntity.ok().body(Message.write("SUCCESS", response));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
            
        }
    }

    public ResponseEntity<Message> PutGit(GithubPutDto githubPutDto, String accessToken) {
        try {

            GithubPutResponseDto response = gitHubPut(githubPutDto, accessToken);

            if (response == null) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR"));
            }

            return ResponseEntity.ok().body(Message.write("SUCCESS", response));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
            
        }
    }

    // github에 create Repository 요청하기
    private GithubRepositoryResponseDTO getParameter(GithubRepositoryMakeDTO RepoMakeDTO, String accessToken) {

        RestTemplate restTemplate = new RestTemplate();

        try {
            
        String url = "https://api.github.com/user/repos";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<GithubRepositoryMakeDTO> entity = new HttpEntity<>(RepoMakeDTO, headers);
        
        ResponseEntity<GithubRepositoryResponseDTO> response = restTemplate.exchange(url, HttpMethod.POST, entity,
                GithubRepositoryResponseDTO.class);

        return response.getBody();

        } catch (RestClientException e) {

            return null;
        }
    }
    
    // github에 Repository에 글 남기기
    private GithubPutResponseDto gitHubPut(GithubPutDto githubPutDto, String accessToken) {

        RestTemplate restTemplate = new RestTemplate();

        try {

            String owner = githubPutDto.getOwner();
            String repo = githubPutDto.getRepo();
            String path = githubPutDto.getPath();
            String url = String.format("https://api.github.com/repos/%s/%s/contents/%s", owner, repo, path);

            // Base64 encoding for content
            String originalContent = githubPutDto.getContent();
            String encodedContent = Base64.getEncoder().encodeToString(originalContent.getBytes());
            githubPutDto.setContent(encodedContent);

            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            HttpEntity<GithubPutDto> entity = new HttpEntity<>(githubPutDto, headers);

            
            ResponseEntity<GithubPutResponseDto> response = restTemplate.exchange(url, HttpMethod.PUT, entity,
                    GithubPutResponseDto.class);

            return response.getBody();

        } catch (RestClientException e) {

            return null;
        }
    }
}
