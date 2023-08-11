package com.blograss.blograsslive.apis.postImage;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blograss.blograsslive.apis.postImage.object.PostImage;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PostImageServiceImpl implements PostImageService {

    @Autowired
    private PostImageRepository postImageRepository;

    @Override
    public void insertImages(List<PostImage> images, String postId) {

        for(int i = 0; i < images.size(); i++) {
            String postImageId = UUID.randomUUID().toString();
            images.get(i).setPostId(postId);
            images.get(i).setImageId(postImageId);
        }
        
        postImageRepository.saveAll(images);
    }

    @Override
    public void deleteImages(String postId) {
        postImageRepository.deleteByPostId(postId);
    }
    
}
