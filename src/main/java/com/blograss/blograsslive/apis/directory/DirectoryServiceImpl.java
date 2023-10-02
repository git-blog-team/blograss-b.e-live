package com.blograss.blograsslive.apis.directory;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blograss.blograsslive.apis.auth.object.User;
import com.blograss.blograsslive.apis.directory.object.Directory;
import com.blograss.blograsslive.apis.directory.object.dto.DirectoryDetailDto;
import com.blograss.blograsslive.apis.post.PostRepository;
import com.blograss.blograsslive.apis.post.object.Post;
import com.blograss.blograsslive.commons.response.Message;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DirectoryServiceImpl implements DirectoryService {

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public ResponseEntity<Message> postDirectory(Directory directory) {
        try {
            String uuid = UUID.randomUUID().toString();

            directory.setDirectoryId(uuid);

            directoryRepository.save(directory);

            return ResponseEntity.ok().body(Message.write("SUCCESS", directory.getDirectoryId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> findDirectoryByUser(User user) {
        try {
            List<Directory> directories = directoryRepository.findByUser(user);

            return ResponseEntity.ok().body(Message.write("SUCCESS", directories));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> findDirectoryByUserDetail(String directoryId) {
        try {
            Directory directory = directoryRepository.findByDirectoryId(directoryId);

            List<Post> posts = postRepository.findByDirectroy(directoryId);

            DirectoryDetailDto directoryDetailDto = new DirectoryDetailDto();

            directoryDetailDto.setDirectoryId(directory.getDirectoryId());
            directoryDetailDto.setName(directory.getName());
            directoryDetailDto.setCteatedAt(directory.getCreatedAt());
            directoryDetailDto.setPosts(posts);

            return ResponseEntity.ok().body(Message.write("SUCCESS", directoryDetailDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }

    @Override
    public ResponseEntity<Message> deleteDirectory(String directoryId) {
        try {
            
            directoryRepository.deleteByDirectoryId(directoryId);

            List<Post> posts = postRepository.findByDirectroy(directoryId);

            for(Post post : posts) {
                post.setDirectroy(null);
            }

            postRepository.saveAll(posts);
            
            return ResponseEntity.ok().body(Message.write("SUCCESS"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.write("INTERNAL_SERVER_ERROR", e));
        }
    }
    
}
