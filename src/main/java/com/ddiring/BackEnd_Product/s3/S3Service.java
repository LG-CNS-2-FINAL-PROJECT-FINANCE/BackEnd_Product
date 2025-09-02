package com.ddiring.BackEnd_Product.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;  // AWS S3 클라이언트

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 업로드 (등록/수정 단계에서 사용)
    public String uploadFile(MultipartFile file) throws IOException {
        String fileUrl = "files/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        PutObjectRequest por = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileUrl)
                .contentType(file.getContentType())
                // .acl("public-read") 공개처리 코드
                .build();
        s3Client.putObject(por, RequestBody.fromBytes(file.getBytes()));
        return "https://" + bucket + ".s3.amazonaws.com/" + fileUrl;
    }

    // 단일 파일 삭제 (승인/거절 단계에서 사용) >> 일단 보류
    public void deleteFile(String fileUrl) {
        String key = fileUrl.replace("https://" + bucket + ".s3.amazonaws.com/", "");
        DeleteObjectRequest dor = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(dor);
    }
}
