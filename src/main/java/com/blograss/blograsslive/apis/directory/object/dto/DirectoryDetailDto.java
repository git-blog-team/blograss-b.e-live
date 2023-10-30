package com.blograss.blograsslive.apis.directory.object.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.blograss.blograsslive.apis.post.object.Post;

import lombok.Data;

@Data
public class DirectoryDetailDto {
    
    private String directoryId;

    private String name;

    private LocalDateTime cteatedAt;

    private List<Post> posts;
}
