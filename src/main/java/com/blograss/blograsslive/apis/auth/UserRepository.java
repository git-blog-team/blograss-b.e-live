package com.blograss.blograsslive.apis.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blograss.blograsslive.apis.auth.object.User;

public interface UserRepository extends JpaRepository<User,Integer>{

    void save(String userId);

    User findByUserId(String userId);
    
}
