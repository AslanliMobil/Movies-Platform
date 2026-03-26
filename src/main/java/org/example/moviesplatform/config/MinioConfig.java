package org.example.moviesplatform.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    // Əgər YAML-dan oxuya bilməsə, qoşa nöqtədən sonrakı dəyəri götürəcək
    @Value("${spring.minio.url:http://localhost:9000}")
    private String url;

    @Value("${spring.minio.access-key:minioadmin}")
    private String accessKey;

    @Value("${spring.minio.secret-key:minioadmin}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}