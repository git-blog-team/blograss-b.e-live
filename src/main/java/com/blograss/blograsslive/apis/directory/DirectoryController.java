package com.blograss.blograsslive.apis.directory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blograss.blograsslive.apis.auth.object.User;
import com.blograss.blograsslive.apis.directory.object.Directory;
import com.blograss.blograsslive.commons.response.Message;
import com.blograss.blograsslive.commons.utils.EtcUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dir")
public class DirectoryController {

    @Autowired
    private EtcUtils etcUtils;

    @Autowired
    private DirectoryService directoryService;
    
    @PostMapping
    public ResponseEntity<Message> postDirectory(
        @RequestBody Directory directory,
        HttpServletRequest req
    ) {
        String userId = etcUtils.getUserIdByAccessToken(req);

        User user = new User();
        user.setUserId(userId);

        directory.setUser(user);

        return directoryService.postDirectory(directory);
    }

    @GetMapping
    public ResponseEntity<Message> findDirectoryByUser(
        @RequestParam String userId
    ) {

        User user = new User();
        user.setUserId(userId);

        return directoryService.findDirectoryByUser(user);
    }

    @GetMapping("/detail")
    public ResponseEntity<Message> findDirectoryByUserDetail(
        @RequestParam String directoryId
    ) {
        return directoryService.findDirectoryByUserDetail(directoryId);
    }

    @DeleteMapping
    public ResponseEntity<Message> deleteDirectory(
        @RequestBody Directory directory
    ) {

        return directoryService.deleteDirectory(directory.getDirectoryId());
    }
}