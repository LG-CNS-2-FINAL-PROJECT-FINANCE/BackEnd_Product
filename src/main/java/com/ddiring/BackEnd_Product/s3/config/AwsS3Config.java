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
        // 1) 환경변수로 넣어준 경우(권장): DefaultCredentialsProvider
        AwsCredentialsProvider provider = DefaultCredentialsProvider.create();

        // 2) 혹시 YAML로 강제로 지정하고 싶다면 (비추)
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
                .region(Region.of(props.getRegion().getStatic()))
                .credentialsProvider(provider)
                .build();
    }
}
