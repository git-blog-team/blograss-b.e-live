package com.blograss.blograsslive.apis.post.object;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.blograss.blograsslive.apis.auth.object.User;
import com.blograss.blograsslive.apis.postImage.object.PostImage;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "post")
public class Post {

    @Id
    @Column(name = "postid")
    private String postId;

    @Column(name = "title")
    private String title;

    @Column(name = "urlslug")
    private String urlSlug;

    @Column(name = "content")
    private String content;

    @Column(name = "description")
    private String description;

    @Column(name = "reportcount")
    private Integer reportCount;

    @Column(name = "directory")
    private String directroy;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userid")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "postid")
    private List<PostImage> images;

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;
}
