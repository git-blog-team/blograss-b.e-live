package com.blograss.blograsslive.apis.post;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import com.blograss.blograsslive.apis.post.object.Post;
import com.blograss.blograsslive.commons.response.Message;

public interface PostService {
    
    ResponseEntity<Message> save(Post post);

    ResponseEntity<Message> update(Post post);

    ResponseEntity<Message> findAll(String keyword, PageRequest pageRequest);

    ResponseEntity<Message> findById(String postId);

    ResponseEntity<Message> delete(Post post);
}
