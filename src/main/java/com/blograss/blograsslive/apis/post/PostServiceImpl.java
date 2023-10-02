package com.blograss.blograsslive.apis.post;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blograss.blograsslive.apis.auth.object.User;
import com.blograss.blograsslive.apis.post.object.Post;
import com.blograss.blograsslive.apis.post.object.dto.PostDetailDto;
import com.blograss.blograsslive.commons.response.Message;
import com.blograss.blograsslive.commons.utils.EtcUtils;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EtcUtils etcUtils;

    @Override
    public ResponseEntity<Message> findAll(String keyword, PageRequest pageRequest) {
        try {
            Page<Post> result = postRepository.searchPosts(keyword, pageRequest);

            return ResponseEntity.ok().body(Message.write("SUCCESS", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> save(Post post) {
        try {

            User user = post.getUser();

            Post findPost = postRepository.findByTitleAndUser(post.getTitle(), user);

            String urlTitle = post.getTitle().replace(" ", "-");

            if(findPost == null) {
                post.setUrlSlug(urlTitle);
            } else {
                String uuid = etcUtils.generateUUID();
                post.setUrlSlug(urlTitle + "-" + uuid);
            }
            
            String description = etcUtils.extractTextFromHtml(post.getContent());

            post.setDescription(description);

            postRepository.save(post);

            return ResponseEntity.ok().body(Message.write("SUCCESS", post.getUrlSlug()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> findByUserAndUrlSlug(String userId, String urlSlug) {
        try {

            User user = new User();
            user.setUserId(userId);

            Post post = postRepository.findByUserAndUrlSlug(user, urlSlug);

            Post nextPost = postRepository.findFirstPostAfterCreatedAt(post.getCreatedAt(), post.getUser());
            Post prevPost = postRepository.findFirstPostBeforeCreatedAt(post.getCreatedAt(), post.getUser());

            PostDetailDto postDetailDto = new PostDetailDto();

            postDetailDto.setCurrentPost(post);
            postDetailDto.setNextPost(nextPost);
            postDetailDto.setPrevPost(prevPost);

            return ResponseEntity.ok().body(Message.write("SUCCESS", postDetailDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> update(Post post) {
        try {
            Post findPost = postRepository.findByPostId(post.getPostId());

            LocalDateTime now = LocalDateTime.now();

            findPost.setUpdatedAt(now);
            findPost.setImages(post.getImages());

            if(post.getContent() != null) {
                String description = etcUtils.extractTextFromHtml(post.getContent());

                findPost.setContent(post.getContent());

                findPost.setDescription(description);
            }
            if(post.getTitle() != null) {
                findPost.setTitle(post.getTitle());

                String urlTitle = post.getTitle().replace(" ", "-");

                Post urlFindPost = postRepository.findByUserAndUrlSlug(findPost.getUser(), urlTitle);                
                if(urlFindPost == null) {
                    findPost.setUrlSlug(urlTitle);
                } else {
                    String uuid = etcUtils.generateUUID();
                    findPost.setUrlSlug(urlTitle + "-" + uuid);
                }
            }

            postRepository.save(findPost);

            return ResponseEntity.ok().body(Message.write("SUCCESS", findPost.getUrlSlug()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> delete(Post post) {
        try{
            postRepository.deleteByPostId(post.getPostId());

           return ResponseEntity.ok().body(Message.write("SUCCESS"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public Post findPost(String userId, String urlSlug) {
        User user = new User();
        user.setUserId(userId);
        return postRepository.findByUserAndUrlSlug(user, urlSlug);
    }

    @Override
    public ResponseEntity<Message> findPostListByUser(String userId, PageRequest pageRequest) {
        try {
            User user = new User();
            user.setUserId(userId);

            Page<Post> postList = postRepository.findByUserOrderByCreatedAtDesc(user, pageRequest);

            return ResponseEntity.ok().body(Message.write("SUCCESS", postList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }
    
}
