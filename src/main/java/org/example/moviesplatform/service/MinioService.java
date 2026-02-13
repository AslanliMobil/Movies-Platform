package org.example.moviesplatform.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    // YAML-dan oxuya bilməsə, 'movie-videos' istifadə edəcək
    @Value("${minio.bucket-name:movie-videos}")
    private String bucketName;

    public void uploadHlsFolder(String movieId, Path hlsFolder) {
        log.info("HLS qovluğu MinIO-ya yüklənir: {}", hlsFolder);
        try {
            Files.walk(hlsFolder)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            String objectName = "movies/" + movieId + "/" + file.getFileName().toString();

                            // Content Type teyini
                            String contentType = file.toString().endsWith(".m3u8")
                                    ? "application/x-mpegURL" : "video/MP2T";

                            try (InputStream is = Files.newInputStream(file)) {
                                minioClient.putObject(
                                        PutObjectArgs.builder()
                                                .bucket(bucketName)
                                                .object(objectName)
                                                .stream(is, Files.size(file), -1)
                                                .contentType(contentType)
                                                .build()
                                );
                            }
                            log.info("📤 MinIO-ya yükləndi: {}", objectName);
                        } catch (Exception e) {
                            log.error("❌ Fayl yüklənərkən xəta: {}", file.getFileName(), e);
                        }
                    });

            log.info("✅ Movie ID {} üçün bütün HLS faylları MinIO-ya uğurla yükləndi.", movieId);

            // Opsional: Yükləmə bitdikdən sonra lokal temp qovluğunu təmizlə
            // cleanUpTempFolder(hlsFolder);

        } catch (Exception e) {
            log.error("❌ Qovluq gəzilərkən xəta: ", e);
        }
    }
}