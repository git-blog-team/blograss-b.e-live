package com.blograss.blograsslive.apis.image;

import java.time.Instant;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.blograss.blograsslive.commons.response.Message;

@RestController
@RequestMapping("/image")
public class ImageUploadController {
    
     @Autowired
    AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping
    public ResponseEntity<Message> upload(MultipartFile file) throws Exception {


        String extension = ""; // 파일 확장자 변수
        String originalName = file.getOriginalFilename();

        // 원본 파일 이름에서 확장자를 가져오기.
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
        extension = originalName.substring(dotIndex);
        }
        Random rnd = new Random();
        String randomTemp = String.valueOf((char) ((int) (rnd.nextInt(26)) + 65));
        
        //timestamp로 새로운 UUID 만들기
        String newName = "B-" + Instant.now().getEpochSecond() + randomTemp;

        String bucketPath = "images/" + newName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        
        // S3에 업로드 하기
		amazonS3Client.putObject(
			new PutObjectRequest(bucket, bucketPath, file.getInputStream(), metadata)
				.withCannedAcl(CannedAccessControlList.PublicRead)
		);
        
        
        String imagePath = amazonS3Client.getUrl(bucket, bucketPath).toString(); // 접근가능한 URL 가져오기
        
        if (imagePath != null) {
            return ResponseEntity.ok(Message.write("SUCCESS", newName));
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(Message.write("FAIL"));
        }
    
    }

}
