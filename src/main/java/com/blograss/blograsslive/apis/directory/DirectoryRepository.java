package com.blograss.blograsslive.apis.directory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blograss.blograsslive.apis.directory.object.Directory;
import java.util.List;
import com.blograss.blograsslive.apis.auth.object.User;


public interface DirectoryRepository extends JpaRepository<Directory, Integer>{
    
    List<Directory> findByUser(User user);

    Directory findByDirectoryId(String directoryId);

    void deleteByDirectoryId(String directoryId);
}
