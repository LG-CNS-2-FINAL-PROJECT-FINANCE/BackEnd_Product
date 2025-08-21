package com.ddiring.BackEnd_Product.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;  // AWS S3 클라이언트

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file, String fileUrl) throws IOException{
        if(fileUrl.isBlank()){
            fileUrl = "profiles/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        }
        else{
            fileUrl = fileUrl.replace("https://" + bucket + ".s3.amazonaws.com/","");
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileUrl)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        return "https://" + bucket + ".s3.amazonaws.com/" + fileUrl;
    }
}
