package com.blograss.blograsslive.apis.postImage.object;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@DynamicInsert
@Table(name = "postimage")
public class PostImage {
    
    @Id
    @Column(name = "imageid")
    private String imageId;

    @Column(name = "url")
    private String url;

    @Column(name = "postid")
    private String postId;

}
