package com.blograss.blograsslive.apis.postImage;

import java.util.List;

import com.blograss.blograsslive.apis.postImage.object.PostImage;

public interface PostImageService {
    
    void insertImages(List<PostImage> images, String postId);

    void deleteImages(String postId);

    
}
