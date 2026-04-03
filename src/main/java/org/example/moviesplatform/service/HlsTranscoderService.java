package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.repository.MovieRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class HlsTranscoderService {

    private final MinioService minioService;
    private final MovieRepository movieRepository;

    private final String FFMPEG_PATH = "C:\\Myprojects\\ffmpeg-8.0.1-full_build\\bin\\ffmpeg.exe";

    @Async
    @Transactional
    public void convertToHls(String movieId, String inputMp4Path) {
        try {
            log.info("🎬 Video transcoding başladı. Movie ID: {}, Path: {}", movieId, inputMp4Path);

            Path outputDir = Paths.get(System.getProperty("java.io.tmpdir"), "movies_hls", movieId);

            if (Files.exists(outputDir)) {
                log.info("🧹 Köhnə temp qovluq təmizlənir: {}", outputDir);
                deleteDirectory(outputDir.toFile());
            }
            Files.createDirectories(outputDir);

            String playlistPath = outputDir.resolve("index.m3u8").toString();

            ProcessBuilder pb = new ProcessBuilder(
                    FFMPEG_PATH,
                    "-i", inputMp4Path,
                    "-codec:v", "libx264",
                    "-profile:v", "baseline",
                    "-level", "3.0",
                    "-codec:a", "aac",
                    "-start_number", "0",
                    "-hls_time", "10",
                    "-hls_list_size", "0",
                    "-f", "hls",
                    playlistPath
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("FFmpeg: {}", line);
                }
            }

            boolean finished = process.waitFor(30, TimeUnit.MINUTES);
            int exitCode = process.exitValue();

            if (finished && exitCode == 0) {
                log.info("✅ Transcoding bitdi. MinIO-ya yükləmə başlayır...");

                minioService.uploadHlsFolder(movieId, outputDir);

                updateMovieDatabase(movieId);

                log.info("🚀 Movie ID {} üçün bütün proseslər tamamlandı!", movieId);

                Files.deleteIfExists(Paths.get(inputMp4Path));
                deleteDirectory(outputDir.toFile());
                log.info("🗑️ Müvəqqəti lokal fayllar təmizləndi.");
            } else {
                log.error("❌ FFmpeg xətası! Exit Code: {}", exitCode);
            }

        } catch (Exception e) {
            log.error("💥 HLS emalı zamanı kritik xəta: ", e);
        }
    }

    private void updateMovieDatabase(String movieId) {
        Integer id = Integer.parseInt(movieId);
        movieRepository.findById(id).ifPresent(movie -> {
            String videoUrl = "http://localhost:9000/movie-videos/" + movieId + "/index.m3u8";
            movie.setVideoUrl(videoUrl);
            movieRepository.save(movie);
            log.info("📝 Verilənlər bazasında video_url yeniləndi: {}", videoUrl);
        });
    }

    private void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}