package org.example.moviesplatform.controller;

import io.minio.GetObjectArgs;
import io.minio.MinioClient; // BU IMPORT ÇATIŞMIRDI
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoStreamController {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name:movie-videos}") // Bucket adını birbaşa kodda yazmaq yerinə properties-dən götürək
    private String bucketName;

    @GetMapping("/stream/{movieId}/{fileName}")
    public ResponseEntity<InputStreamResource> getHlsStream(
            @PathVariable String movieId,
            @PathVariable String fileName) throws Exception {

        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object("movies/" + movieId + "/" + fileName)
                        .build()
        );

        String contentType = fileName.endsWith(".m3u8")
                ? "application/x-mpegURL" : "video/MP2T";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(new InputStreamResource(stream));
    }
}