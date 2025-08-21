package com.ddiring.BackEnd_Product.s3.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3StorageService {

    private final S3Client s3;
    private final com.ddiring.BackEnd_Product.s3.config.AwsProps props;

    public UploadResult upload(byte[] bytes, String originalFilename, String contentType, String folder) {
        String bucket = props.getS3().getBucket();
        String yyyy = String.valueOf(LocalDate.now().getYear());
        String mm = String.format("%02d", LocalDate.now().getMonthValue());

        String safeName = (originalFilename == null || originalFilename.isBlank())
                ? "file" : StringUtils.getFilename(originalFilename);
        String key = String.format("%s/%s/%s/%s_%s",
                folder == null ? "temp" : folder, yyyy, mm, UUID.randomUUID(), safeName);

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .acl(ObjectCannedACL.PUBLIC_READ) // 공개 URL 필요 시
                .build();

        s3.putObject(req, RequestBody.fromBytes(bytes));

        String url = "https://" + bucket + ".s3." + props.getRegion() + ".amazonaws.com/"
                + URLEncoder.encode(key, StandardCharsets.UTF_8).replace("+", "%20");

        return new UploadResult(key, url, contentType, bytes.length);
    }

    public record UploadResult(String key, String url, String contentType, long size) {}
}
