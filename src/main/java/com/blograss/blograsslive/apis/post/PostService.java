package com.blograss.blograsslive.apis.post;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import com.blograss.blograsslive.apis.post.object.Post;
import com.blograss.blograsslive.commons.response.Message;

public interface PostService {
    
    ResponseEntity<Message> save(Post post);

    ResponseEntity<Message> update(Post post);

    ResponseEntity<Message> findAll(String keyword, PageRequest pageRequest);

    ResponseEntity<Message> findByUserAndUrlSlug(String userId, String urlSlug);

    ResponseEntity<Message> findPostListByUser(String userId, PageRequest pageRequest);

    ResponseEntity<Message> delete(Post post);

    Post findPost(String userId, String urlSlug);

    Post findPostById(String postId);

    List<Post> findDirectoryList(String directoryId);

}
