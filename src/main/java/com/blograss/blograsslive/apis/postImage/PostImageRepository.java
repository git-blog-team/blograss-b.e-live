package com.blograss.blograsslive.apis.postImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blograss.blograsslive.apis.postImage.object.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
    
    @Modifying
    @Query("DELETE FROM PostImage e WHERE e.postId = :postId")
    void deleteByPostId(@Param("postId") String postId);
}
