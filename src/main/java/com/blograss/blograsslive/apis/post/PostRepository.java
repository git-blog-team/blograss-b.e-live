package com.blograss.blograsslive.apis.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blograss.blograsslive.apis.post.object.Post;
import com.blograss.blograsslive.apis.user.object.User;


public interface PostRepository extends JpaRepository<Post, Integer> {

    // void updateByPostId(Post post);
    
    Post findByPostId(String postId);

    void deleteByPostId(String postId);

    List<Post> findByUser(User user);

    @Query("SELECT p FROM Post p WHERE (:keyword IS NULL OR p.content LIKE %:keyword%)")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);
}
