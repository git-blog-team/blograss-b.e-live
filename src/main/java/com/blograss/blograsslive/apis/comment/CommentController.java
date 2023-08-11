package com.blograss.blograsslive.apis.comment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blograss.blograsslive.apis.comment.object.Comment;
import com.blograss.blograsslive.apis.user.object.User;
import com.blograss.blograsslive.commons.response.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/comment")
public class CommentController {

    private String userId = "uiop5487@gmail.com";

    @Autowired
    private CommentService commentService;
    
    @GetMapping
    public ResponseEntity<Message> findCommentList(
        @RequestParam String postId,
        @RequestParam Integer page,
        @RequestParam Integer limit,
        @RequestParam String sortField,
        @RequestParam String sortOrder
    ) {
        page = page - 1;

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
        PageRequest pageRequest = PageRequest.of(page, limit, sort);

        return commentService.findCommentList(postId, pageRequest);
    }

    @PostMapping
    public ResponseEntity<Message> postComment(@RequestBody Comment comment) {

        User user = new User();
        user.setUserId(userId);
        comment.setUser(user);

        return commentService.save(comment);
    }

    @PutMapping
    public ResponseEntity<Message> putComment(@RequestBody Comment comment) {

        return commentService.update(comment, userId);
    }

    @DeleteMapping
    public ResponseEntity<Message> deleteComment(@RequestBody Comment comment) {

        return commentService.delete(comment, userId);
    }
    
}
