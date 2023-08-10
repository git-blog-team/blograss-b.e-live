package com.blograss.blograsslive.apis.post;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blograss.blograsslive.apis.post.object.Post;
import com.blograss.blograsslive.commons.response.Message;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

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

            postRepository.save(post);

            return ResponseEntity.ok().body(Message.write("SUCCESS", post.getPostId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> findById(String postId) {
        try {
            Post post = postRepository.findByPostId(postId);

            return ResponseEntity.ok().body(Message.write("SUCCESS", post));
        } catch (Exception e) {
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

            if(post.getContent() != null){
                findPost.setContent(post.getContent());
            }
            if(post.getTitle() != null) {
                findPost.setTitle(post.getTitle());
            }

            postRepository.save(findPost);

            return ResponseEntity.ok().body(Message.write("SUCCESS", findPost.getPostId()));
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
    
}
