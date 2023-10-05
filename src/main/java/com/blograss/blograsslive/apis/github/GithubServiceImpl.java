package com.blograss.blograsslive.apis.github;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.blograss.blograsslive.apis.github.object.dto.githubAppUserDto.GithubAppInfoDto;
import com.blograss.blograsslive.apis.github.object.dto.githubAppUserDto.GithubAppUserResponseDto;
import com.blograss.blograsslive.apis.github.object.dto.githubDeleteDto.GithubDeleteContentsDto;
import com.blograss.blograsslive.apis.github.object.dto.githubGetContentsDto.GithubGetContentsResponseDto;
import com.blograss.blograsslive.apis.github.object.dto.githubPutDto.GithubPutDto;
import com.blograss.blograsslive.apis.github.object.dto.githubPutDto.GithubPutResponseDto;
import com.blograss.blograsslive.apis.github.object.dto.githubRepositoryDto.GithubRepositoryMakeDTO;
import com.blograss.blograsslive.apis.github.object.dto.githubRepositoryDto.GithubRepositoryResponseDTO;

@Service
public class GithubServiceImpl implements GithubService {

    @Value("${github.app.id}")
    private String appId;

    @Value("${github.app.slug}")
    private String appSlug;
    
    public GithubRepositoryResponseDTO createRepo(String accessToken) {

        GithubRepositoryMakeDTO RepoMakeDTO = new GithubRepositoryMakeDTO();

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
            e.printStackTrace();
            return null;
        }

    }

    public GithubPutResponseDto putGit(GithubPutDto githubPutDto, String accessToken) {

        RestTemplate restTemplate = new RestTemplate();

        try {

            String owner = githubPutDto.getOwner();
            String repo = githubPutDto.getRepo();
            String path = githubPutDto.getPath() + ".md";
            String url = String.format("https://api.github.com/repos/%s/%s/contents/%s", owner, repo, path);

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
            e.printStackTrace();
            return null;
        }
    }

    public GithubRepositoryResponseDTO getRepo(String owner, String accessToken) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = String.format("https://api.github.com/repos/%s/blograss", owner);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<GithubRepositoryResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                GithubRepositoryResponseDTO.class
            );

            GithubRepositoryResponseDTO githubRepositoryGetResponseDto = response.getBody();

            return githubRepositoryGetResponseDto;   
        } catch (Exception e) {
            return null;
        }
    }

    public GithubGetContentsResponseDto getRepoContent(GithubPutDto githubPutDto, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        try {

            String owner = githubPutDto.getOwner();
            String repo = githubPutDto.getRepo();
            String path = githubPutDto.getPath() + ".md";
            String url = String.format("https://api.github.com/repos/%s/%s/contents/%s", owner, repo, path);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            HttpEntity<GithubPutDto> entity = new HttpEntity<>(headers);

            
            ResponseEntity<GithubGetContentsResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, entity,
            GithubGetContentsResponseDto.class);

            return response.getBody();

        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<GithubGetContentsResponseDto> getRepoContents(GithubPutDto githubPutDto, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        try {

            String owner = githubPutDto.getOwner();
            String repo = githubPutDto.getRepo();
            String path = githubPutDto.getPath();
            String url = String.format("https://api.github.com/repos/%s/%s/contents/%s", owner, repo, path);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            HttpEntity<GithubPutDto> entity = new HttpEntity<>(headers);

            
            ResponseEntity<List<GithubGetContentsResponseDto>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
            new ParameterizedTypeReference<List<GithubGetContentsResponseDto>>() {});

            return response.getBody();

        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean validInstallGithubApp(String accessToken) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "https://api.github.com/user/installations";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<GithubAppUserResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                GithubAppUserResponseDto.class
            );

            response.getStatusCode();

            GithubAppUserResponseDto githubRepositoryGetResponseDto = response.getBody();

            if(githubRepositoryGetResponseDto.getTotal_count() == 0) {
                return false;
            }

            List<GithubAppInfoDto> githubAppInfos = githubRepositoryGetResponseDto.getInstallations();

            for(GithubAppInfoDto githubAppInfo : githubAppInfos) {
                if(githubAppInfo.getApp_id().matches(appId)) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String deleteContents(GithubPutDto githubPutDto, String accessToken, String sha) {
        RestTemplate restTemplate = new RestTemplate();

        try {

            String owner = githubPutDto.getOwner();
            String repo = githubPutDto.getRepo();
            String path = githubPutDto.getPath() + ".md";
            String url = String.format("https://api.github.com/repos/%s/%s/contents/%s", owner, repo, path);

            GithubDeleteContentsDto githubDeleteContentsDto = new GithubDeleteContentsDto();

            githubDeleteContentsDto.setSha(sha);

            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            HttpEntity<GithubDeleteContentsDto> entity = new HttpEntity<>(githubDeleteContentsDto, headers);

             
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity,
                    String.class);

            return response.getBody();

        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
