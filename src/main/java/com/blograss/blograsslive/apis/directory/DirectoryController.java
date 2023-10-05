package com.blograss.blograsslive.apis.directory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blograss.blograsslive.apis.auth.object.User;
import com.blograss.blograsslive.apis.directory.object.Directory;
import com.blograss.blograsslive.apis.github.GithubService;
import com.blograss.blograsslive.apis.github.object.dto.githubGetContentsDto.GithubGetContentsResponseDto;
import com.blograss.blograsslive.apis.github.object.dto.githubPutDto.GithubPutDto;
import com.blograss.blograsslive.apis.github.object.dto.githubRepositoryDto.GithubRepositoryResponseDTO;
import com.blograss.blograsslive.apis.post.PostService;
import com.blograss.blograsslive.apis.post.object.Post;
import com.blograss.blograsslive.commons.response.Message;
import com.blograss.blograsslive.commons.utils.EtcUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dir")
public class DirectoryController {

    @Autowired
    private EtcUtils etcUtils;

    @Autowired
    private GithubService githubService;

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    private PostService postService;
    
    @PostMapping
    public ResponseEntity<Message> postDirectory(
        @RequestBody Directory directory,
        HttpServletRequest req
    ) {
        String userId = etcUtils.getUserIdByAccessToken(req);

        User user = new User();
        user.setUserId(userId);

        directory.setUser(user);

        Directory directory2 = directoryService.findDirectoryByNameAndUserId(user, directory.getName());

        if(directory2 != null) {
            return ResponseEntity.badRequest().body(Message.write("Duplicate Directory Name"));
        }

        return directoryService.postDirectory(directory);
    }

    @GetMapping
    public ResponseEntity<Message> findDirectoryByUser(
        @RequestParam String userId
    ) {

        User user = new User();
        user.setUserId(userId);

        return directoryService.findDirectoryByUser(user);
    }

    @GetMapping("/detail")
    public ResponseEntity<Message> findDirectoryByUserDetail(
        @RequestParam String directoryId
    ) {
        return directoryService.findDirectoryByUserDetail(directoryId);
    }

    @DeleteMapping
    public ResponseEntity<Message> deleteDirectory(
        @RequestBody Directory directory,
        HttpServletRequest req
    ) {

        String userId = etcUtils.getUserIdByAccessToken(req);

        String accessToken = etcUtils.getAccessToken(req);

        // 깃헙앱을 설치를 했는가
        Boolean isInstalled = githubService.validInstallGithubApp(accessToken);

        if(isInstalled) {
            
            GithubRepositoryResponseDTO githubRepositoryGetResponseDto = githubService.getRepo(userId, accessToken);

            if(githubRepositoryGetResponseDto != null) {
                String name = directoryService.getDirectoryName(directory.getDirectoryId());

                GithubPutDto githubPutDto = new GithubPutDto();
                githubPutDto.setOwner(userId);
                githubPutDto.setPath(name);

                List<GithubGetContentsResponseDto> githubGetContentsResponseDtos = githubService.getRepoContents(githubPutDto, accessToken);

                for(GithubGetContentsResponseDto res : githubGetContentsResponseDtos) {
                    String path = res.getPath().replaceFirst("\\.md$", "");
                    String sha = res.getSha();

                    githubPutDto.setPath(path);

                    githubService.deleteContents(githubPutDto, accessToken, sha);
                }

                List<Post> posts = postService.findDirectoryList(directory.getDirectoryId());

                for(Post post : posts) {
                    String path = post.getTitle() + "-" + post.getPostId();
                    String contents = post.getContent();

                    githubPutDto.setPath(path);
                    githubPutDto.setContent(contents);

                    githubService.putGit(githubPutDto, accessToken);
                }


            }

        }

        return directoryService.deleteDirectory(directory.getDirectoryId());
    }
}