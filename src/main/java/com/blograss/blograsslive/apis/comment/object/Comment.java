package com.blograss.blograsslive.apis.comment.object;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.blograss.blograsslive.apis.auth.object.User;
import com.blograss.blograsslive.apis.post.object.Post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "comment")
public class Comment {

    @Id
    @Column(name = "commentid")
    private String commentId;

    @Column(name = "content")
    private String content;

    @Column(name = "reportcount")
    private Integer reportCount;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne(targetEntity = Post.class)
    @JoinColumn(name = "postid")
    private Post post;

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;

}
