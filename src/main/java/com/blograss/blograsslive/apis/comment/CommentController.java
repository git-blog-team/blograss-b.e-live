package com.blograss.blograsslive.apis.comment;

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
import com.blograss.blograsslive.apis.comment.object.Comment;
import com.blograss.blograsslive.commons.response.Message;
import com.blograss.blograsslive.commons.utils.RedisUtil;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisUtil redisUtil;
    
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
    public ResponseEntity<Message> postComment(
        @RequestBody Comment comment,
        HttpServletRequest req
    ) {

        String token = req.getHeader("Authorization");

        String accessToken = token.split(" ")[1];

        String userId = (String) redisUtil.get(accessToken);

        User user = new User();
        user.setUserId(userId);
        comment.setUser(user);

        return commentService.save(comment);
    }

    @PutMapping
    public ResponseEntity<Message> putComment(
        @RequestBody Comment comment,
        HttpServletRequest req
    ) {

        String token = req.getHeader("Authorization");

        String accessToken = token.split(" ")[1];

        String userId = (String) redisUtil.get(accessToken);

        return commentService.update(comment, userId);
    }

    @DeleteMapping
    public ResponseEntity<Message> deleteComment(
        @RequestBody Comment comment,
        HttpServletRequest req
    ) {

        String token = req.getHeader("Authorization");

        String accessToken = token.split(" ")[1];

        String userId = (String) redisUtil.get(accessToken);

        return commentService.delete(comment, userId);
    }
    
}
