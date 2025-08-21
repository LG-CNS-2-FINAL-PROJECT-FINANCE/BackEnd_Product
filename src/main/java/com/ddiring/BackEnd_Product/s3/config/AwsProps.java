package com.ddiring.BackEnd_Product.s3.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "cloud.aws")
public class AwsProps {
    private Credentials credentials = new Credentials();
    private Region region = new Region();
    private S3 s3 = new S3();

    @Getter @Setter
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }
    @Getter @Setter
    public static class Region {
        private String static_; // 'static' 키워드는 자바에서 쓰기 애매해서 뒤에 '_'를 붙임
        public String getStatic() { return static_; }
    }
    @Getter @Setter
    public static class S3 {
        private String bucket;
    }
}
