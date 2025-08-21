package com.ddiring.BackEnd_Product.s3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {

    @Bean
    public S3Client s3Client(AwsProps props) {
        // 1) 환경변수/EC2 IAM Role/EKS IRSA 등에서 가져오기 (권장)
        AwsCredentialsProvider provider = DefaultCredentialsProvider.create();

        // 2) application.yml 로 직접 지정했을 경우 fallback
        if (props.getCredentials().getAccessKey() != null &&
                !props.getCredentials().getAccessKey().isBlank()) {
            provider = StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                            props.getCredentials().getAccessKey(),
                            props.getCredentials().getSecretKey()
                    )
            );
        }

        return S3Client.builder()
                .region(Region.of(props.getRegion()))   // ✅ region 단일 값
                .credentialsProvider(provider)          // ✅ provider 지정
                .build();
    }
}
