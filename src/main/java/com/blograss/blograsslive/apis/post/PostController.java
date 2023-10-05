package com.blograss.blograsslive.apis.post;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blograss.blograsslive.apis.auth.object.User;
import com.blograss.blograsslive.apis.directory.DirectoryService;
import com.blograss.blograsslive.apis.github.GithubService;
import com.blograss.blograsslive.apis.github.object.dto.githubGetContentsDto.GithubGetContentsResponseDto;
import com.blograss.blograsslive.apis.github.object.dto.githubPutDto.GithubPutDto;
import com.blograss.blograsslive.apis.github.object.dto.githubRepositoryDto.GithubRepositoryResponseDTO;
import com.blograss.blograsslive.apis.post.object.Post;
import com.blograss.blograsslive.apis.postImage.PostImageService;
import com.blograss.blograsslive.apis.postImage.object.PostImage;
import com.blograss.blograsslive.commons.response.Message;
import com.blograss.blograsslive.commons.utils.EtcUtils;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostImageService postImageService;

    @Autowired
    private EtcUtils etcUtils;

    @Autowired
    private GithubService githubService;

    @Autowired
    private DirectoryService directoryService;

    @GetMapping("/list")
    public ResponseEntity<Message> getPostList(
        @RequestParam String keyword,
        @RequestParam Integer page,
        @RequestParam Integer limit,
        @RequestParam String sortField,
        @RequestParam String sortOrder
    ) {
        if (page < 1) {
          return  ResponseEntity.badRequest().body(Message.write("Page is less than 1"));
        }
        page = page - 1;

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
        PageRequest pageRequest = PageRequest.of(page, limit, sort);

        return postService.findAll(keyword, pageRequest); 
    }

    @GetMapping
    public ResponseEntity<Message> getPostById(
        @RequestParam String urlSlug,
        @RequestParam String userId
    ) {
        return postService.findByUserAndUrlSlug(userId, urlSlug);
    }

    @GetMapping("/userlist")
    public ResponseEntity<Message> getPostListByUser(
        @RequestParam String userId,
        @RequestParam Integer page,
        @RequestParam Integer limit,
        @RequestParam String sortField,
        @RequestParam String sortOrder
    ) {
        if (page < 1) {
          return  ResponseEntity.badRequest().body(Message.write("Page is less than 1"));
        }
        page = page - 1;

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
        PageRequest pageRequest = PageRequest.of(page, limit, sort);

        return postService.findPostListByUser(userId, pageRequest);
    }

    @PostMapping
    public ResponseEntity<Message> postPost(
        @RequestBody Post post,
        HttpServletRequest req
    ) {

        String accessToken = etcUtils.getAccessToken(req);

        String userId = etcUtils.getUserIdByAccessToken(req);

        String postId = UUID.randomUUID().toString();

        // 깃헙앱을 설치를 했는가
        Boolean isInstalled = githubService.validInstallGithubApp(accessToken);

        if(isInstalled) {
            // 레포가 있는가
            GithubRepositoryResponseDTO githubRepositoryGetResponseDto = githubService.getRepo(userId, accessToken);

            // 레포 없으면 생성
            if(githubRepositoryGetResponseDto == null) {
                githubService.createRepo(accessToken);
            }

            GithubPutDto githubPutDto = new GithubPutDto();

            githubPutDto.setOwner(userId);
            githubPutDto.setContent(post.getContent());
            // 디렉토리가 설정되어 있나 확인 있으면 경로 추가
            if(post.getDirectory() == null) {
                githubPutDto.setPath(post.getTitle() + "-" + postId);
            } else {
                String name = directoryService.getDirectoryName(post.getDirectory());
                if(name == null) {
                    githubPutDto.setPath(post.getTitle() + "-" + postId);
                } else {
                    githubPutDto.setPath(name + "/" + post.getTitle() + "-" + postId);
                }
            }

            // 파일 커밋, 푸시
            githubService.putGit(githubPutDto, accessToken);
        }

        List<PostImage> images = post.getImages();

        User user = new User();
        user.setUserId(userId);

        for(int i = 0; i < images.size(); i++) {
            String postImageId = UUID.randomUUID().toString();
            images.get(i).setPostId(postId);
            images.get(i).setImageId(postImageId);
        }

        post.setUser(user);
        post.setPostId(postId);

        return postService.save(post);
    }

    @PutMapping
    public ResponseEntity<Message> updatePost(
        @RequestBody Post post,
        HttpServletRequest req
    ) {
        
        String userId = etcUtils.getUserIdByAccessToken(req);

        String accessToken = etcUtils.getAccessToken(req);

        Post post2 = postService.findPost(userId, post.getUrlSlug());

        if(post2 == null) {
            return ResponseEntity.badRequest().body(Message.write("Not Found Post By urlSlug!"));
        }

        // 깃헙앱을 설치를 했는가
        Boolean isInstalled = githubService.validInstallGithubApp(accessToken);

        String postId = post2.getPostId();

        if(isInstalled) {
            // 레포가 있는가
            GithubRepositoryResponseDTO githubRepositoryGetResponseDto = githubService.getRepo(userId, accessToken);

            // 레포 없으면 생성
            if(githubRepositoryGetResponseDto == null) {
                githubService.createRepo(accessToken);
            } else {
                GithubPutDto githubPutDto = new GithubPutDto();
                githubPutDto.setOwner(userId);
                githubPutDto.setContent(post2.getContent());
                // 디렉토리가 설정되어 있나 확인 있으면 경로 추가
                if(post2.getDirectory() == null) {
                    githubPutDto.setPath(post2.getTitle() + "-" + postId);
                } else {
                    String name = directoryService.getDirectoryName(post2.getDirectory());
                    if(name == null) {
                        githubPutDto.setPath(post2.getTitle() + "-" + postId);
                    } else {
                        githubPutDto.setPath(name + "/" + post2.getTitle() + "-" + postId);
                    }
                }
                
                // 레포 있으면 기존 콘텐츠 조회
                GithubGetContentsResponseDto githubGetContentsResponseDto = githubService.getRepoContent(githubPutDto, accessToken);

                // 조회 내용 삭제
                if(githubGetContentsResponseDto != null) {
                    String sha = githubGetContentsResponseDto.getSha();

                    githubService.deleteContents(githubPutDto, accessToken, sha);
                }
            }

            GithubPutDto githubPutDto = new GithubPutDto();

            githubPutDto.setOwner(userId);

            if(post.getContent() == null) {
                githubPutDto.setContent(post2.getContent());
            } else {
                githubPutDto.setContent(post.getContent());
            }

            if(post.getDirectory() == null) {
                if(post2.getDirectory() == null) {
                    if(post.getTitle() == null) {
                        githubPutDto.setPath(post2.getTitle() + "-" + postId);
                    } else {
                        githubPutDto.setPath(post.getTitle() + "-" + postId);
                    }
                } else {
                    String name = directoryService.getDirectoryName(post2.getDirectory());

                    if(post.getTitle() == null) {
                        githubPutDto.setPath(name + "/" + post2.getTitle() + "-" + postId);
                    } else {
                        githubPutDto.setPath(name + "/" + post.getTitle() + "-" + postId);
                    }    
                }
            } else {
                String name = directoryService.getDirectoryName(post.getDirectory());

                if(post.getTitle() == null) {
                    githubPutDto.setPath(name + "/" + post2.getTitle() + "-" + postId);
                } else {
                    githubPutDto.setPath(name + "/" + post.getTitle() + "-" + postId);
                }
            }

            // 파일 커밋, 푸시
            githubService.putGit(githubPutDto, accessToken);
        }

        post.setPostId(postId);

        postImageService.deleteImages(post.getPostId());

        List<PostImage> images = post.getImages();

        for(int i = 0; i < images.size(); i++) {
            String postImageId = UUID.randomUUID().toString();
            images.get(i).setPostId(post.getPostId());
            images.get(i).setImageId(postImageId);
        }

        return postService.update(post);
    }

    @DeleteMapping
    public ResponseEntity<Message> deletePost(
        @RequestBody Post post, 
        HttpServletRequest req
    ) {
        String accessToken = etcUtils.getAccessToken(req);

        String userId = etcUtils.getUserIdByAccessToken(req);

        // 깃헙앱을 설치를 했는가
        Boolean isInstalled = githubService.validInstallGithubApp(accessToken);

        Post findPost = postService.findPostById(post.getPostId());

        if(isInstalled) {
            // 레포가 있는가
            GithubRepositoryResponseDTO githubRepositoryGetResponseDto = githubService.getRepo(userId, accessToken);

            // 레포가 있으면 삭제
            if(githubRepositoryGetResponseDto != null) {
            
                GithubPutDto githubPutDto = new GithubPutDto();
                githubPutDto.setOwner(userId);
                // 디렉토리가 설정되어 있나 확인 있으면 경로 추가
                if(findPost.getDirectory() == null) {
                    githubPutDto.setPath(findPost.getTitle() + "-" + post.getPostId());
                } else {
                    String name = directoryService.getDirectoryName(findPost.getDirectory());
                    if(name == null) {
                        githubPutDto.setPath(findPost.getTitle() + "-" + post.getPostId());
                    } else {
                        githubPutDto.setPath(name + "/" + findPost.getTitle() + "-" + post.getPostId());
                    }
                }

                // 기존 콘텐츠 조회
                GithubGetContentsResponseDto githubGetContentsResponseDto = githubService.getRepoContent(githubPutDto, accessToken);

                // 조회 내용 삭제
                if(githubGetContentsResponseDto != null) {
                    String sha = githubGetContentsResponseDto.getSha();

                    githubService.deleteContents(githubPutDto, accessToken, sha);
                }
            }
        }



        postImageService.deleteImages(post.getPostId());

        return postService.delete(post);
    }
}
