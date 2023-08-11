package com.blograss.blograsslive.apis.comment;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blograss.blograsslive.apis.comment.object.Comment;
import com.blograss.blograsslive.apis.post.object.Post;
import com.blograss.blograsslive.commons.response.Message;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public ResponseEntity<Message> findCommentList(String postId, PageRequest pageRequest) {

        Post post = new Post();
        post.setPostId(postId);

        try {
            return ResponseEntity.ok().body(Message.write("SUCCESS", commentRepository.findByPost(post, pageRequest)));   
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> save(Comment comment) {

        try {
            String commentId = UUID.randomUUID().toString();

            comment.setCommentId(commentId);

            commentRepository.save(comment);            

            return ResponseEntity.ok().body(Message.write("SUCCESS", commentId));   
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> update(Comment comment, String userId) {

        if(comment.getContent() == null || comment.getContent().matches("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.write("NOT_NULL_CONTENT"));
        }

        try{
            Comment findComment = commentRepository.findByCommentId(comment.getCommentId());

            String findUserId = findComment.getUser().getUserId();

            if(!userId.matches(findUserId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.write("MISSMATCH_USER"));
            }

            LocalDateTime now = LocalDateTime.now();

            findComment.setContent(comment.getContent());
            findComment.setUpdatedAt(now);

            commentRepository.save(findComment);
            return ResponseEntity.ok().body(Message.write("SUCCESS", findComment.getCommentId()));   
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> delete(Comment comment, String userId) {

        try{
            Comment findComment = commentRepository.findByCommentId(comment.getCommentId());

            String findUserId = findComment.getUser().getUserId();

            if(!userId.matches(findUserId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.write("MISSMATCH_USER"));
            }

            commentRepository.deleteByCommentId(comment.getCommentId());
            return ResponseEntity.ok().body(Message.write("SUCCESS"));   
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }
    
}
