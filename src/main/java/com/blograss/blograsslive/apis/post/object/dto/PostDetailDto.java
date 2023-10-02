package com.blograss.blograsslive.apis.post.object.dto;

import com.blograss.blograsslive.apis.post.object.Post;

import lombok.Data;

@Data
public class PostDetailDto {
    
    Post currentPost;

    Post nextPost;

    Post prevPost;
}
