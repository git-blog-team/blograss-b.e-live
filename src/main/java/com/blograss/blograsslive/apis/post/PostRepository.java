package com.blograss.blograsslive.apis.post;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blograss.blograsslive.apis.auth.object.User;
import com.blograss.blograsslive.apis.post.object.Post;


public interface PostRepository extends JpaRepository<Post, Integer> {

    // void updateByPostId(Post post);
    
    Post findByPostId(String postId);

    Post findByUserAndUrlSlug(User user, String urlSlug);

    List<Post> findByTitleAndUser(String title, User user);

    void deleteByPostId(String postId);

    List<Post> findByUser(User user);

    @Query("SELECT p FROM Post p WHERE p.user = ?1")
    Page<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    List<Post> findByDirectory(String directory);

    @Query("SELECT p FROM Post p WHERE (:keyword IS NULL OR p.content LIKE %:keyword%)")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.createdAt > ?1 AND p.user = ?2 ORDER BY p.createdAt ASC LIMIT 1")
    Post findFirstPostAfterCreatedAt(LocalDateTime createdAt, User user);

    @Query("SELECT p FROM Post p WHERE p.createdAt < ?1 AND p.user = ?2 ORDER BY p.createdAt DESC LIMIT 1")
    Post findFirstPostBeforeCreatedAt(LocalDateTime createdAt, User user);
}
