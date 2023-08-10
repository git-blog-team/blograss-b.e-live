package com.blograss.blograsslive.apis.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blograss.blograsslive.apis.comment.object.Comment;
import com.blograss.blograsslive.apis.post.object.Post;


public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
    Page<Comment> findByPost(Post post, Pageable pageable);

    Comment findByCommentId(String commentId);

    void deleteByCommentId(String commentId);
}
