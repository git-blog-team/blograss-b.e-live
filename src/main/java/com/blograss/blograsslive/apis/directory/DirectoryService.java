package com.blograss.blograsslive.apis.directory;

import org.springframework.http.ResponseEntity;

import com.blograss.blograsslive.apis.auth.object.User;
import com.blograss.blograsslive.apis.directory.object.Directory;
import com.blograss.blograsslive.commons.response.Message;

public interface DirectoryService {
    
    ResponseEntity<Message> postDirectory(Directory directory);

    ResponseEntity<Message> findDirectoryByUser(User user);

    ResponseEntity<Message> findDirectoryByUserDetail(String directoryId);

    ResponseEntity<Message> deleteDirectory(String directoryId);

    String getDirectoryName(String directoryId);

    Directory findDirectoryByNameAndUserId(User user, String name);
}
