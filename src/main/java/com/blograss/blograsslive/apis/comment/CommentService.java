package com.blograss.blograsslive.apis.comment;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import com.blograss.blograsslive.apis.comment.object.Comment;
import com.blograss.blograsslive.commons.response.Message;

public interface CommentService {
    
    ResponseEntity<Message> findCommentList(String postId, PageRequest pageRequest);

    ResponseEntity<Message> save(Comment comment);

    ResponseEntity<Message> update(Comment comment, String userId);

    ResponseEntity<Message> delete(Comment comment, String userId);
}
