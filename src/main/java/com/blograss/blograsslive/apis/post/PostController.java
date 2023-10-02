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

        String userId = etcUtils.getUserIdByAccessToken(req);

        List<PostImage> images = post.getImages();

        User user = new User();
        user.setUserId(userId);

        String postId = UUID.randomUUID().toString();

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

        Post post2 = postService.findPost(userId, post.getUrlSlug());

        if(post2 == null) {
            return ResponseEntity.badRequest().body(Message.write("Not Found Post By urlSlug!"));
        }
        
        String postId = post2.getPostId();

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
    public ResponseEntity<Message> deletePost(@RequestBody Post post) {

        postImageService.deleteImages(post.getPostId());

        return postService.delete(post);
    }
}
