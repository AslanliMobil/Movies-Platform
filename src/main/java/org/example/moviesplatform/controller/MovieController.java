package org.example.moviesplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.MovieDTO;
import org.example.moviesplatform.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/movies") // Bura "v1" əlavə etdik ki, React-lə eyni olsun
@RequiredArgsConstructor
@Tag(name = "Movie Controller", description = "Filmlərin idarə edilməsi və HLS Video Upload")
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    @Operation(summary = "Bütün filmləri gətir")
    public ResponseEntity<Page<MovieDTO>> getAllMovies(Pageable pageable) {
        return ResponseEntity.ok(movieService.getAllMovies(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID-yə görə film gətir (REDIS)")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Integer id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping(value = "/{id}/upload-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Filmə video yüklə və HLS emalını başlat (ADMIN)")
    public ResponseEntity<String> uploadVideo(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file) throws IOException {

        movieService.processMovieVideo(id, file);
        return ResponseEntity.accepted().body("Video qəbul edildi. Transcoding arxa planda davam edir. Pleyerdə bir neçə dəqiqəyə hazır olacaq.");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Yeni film məlumatlarını yarat (ADMIN)")
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Filmi yenilə (ADMIN)")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Integer id, @Valid @RequestBody MovieDTO dto) {
        return ResponseEntity.ok(movieService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Filmi sil (ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}