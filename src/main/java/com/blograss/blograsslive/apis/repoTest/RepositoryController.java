package com.blograss.blograsslive.apis.repoTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blograss.blograsslive.apis.repoTest.object.dto.githubPutDto.GithubPutDto;
import com.blograss.blograsslive.apis.repoTest.object.dto.githubRepositoryDto.GithubRepositoryMakeDTO;
import com.blograss.blograsslive.commons.response.Message;
import com.blograss.blograsslive.commons.utils.RedisTokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/repo")
public class RepositoryController {
    
    @Autowired
    private RepositoryService repoTestService;

    @Autowired
    private RedisTokenService redisTokenService;

    @PostMapping
    public ResponseEntity<Message> CreateRepo(@RequestBody GithubRepositoryMakeDTO RepoMakeDTO,HttpServletRequest req) {
            
        String token = req.getHeader("Authorization");
        String accessToken = token.split(" ")[1];
           
        return repoTestService.CreateRepo(RepoMakeDTO, accessToken);    
    }
    
    @PutMapping
    public ResponseEntity<Message> PutGit(@RequestBody GithubPutDto githubPutDto, HttpServletRequest req) {

        String token = req.getHeader("Authorization");
        String accessToken = token.split(" ")[1];

        String userId = redisTokenService.getUserId(accessToken);

        if (userId == null) {
            return ResponseEntity.badRequest().body(Message.write("Invalid User"));
        } else {
            githubPutDto.setOwner(userId);
        }
        return repoTestService.PutGit(githubPutDto, accessToken);
    }

     
}
