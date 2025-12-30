package org.example.moviesplatform.controller;

import org.example.moviesplatform.mapper.GenreMapper;
import org.example.moviesplatform.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.GenreDTO;
import org.example.moviesplatform.entity.Genre;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAll() {
        // Service artıq List<GenreDTO> qaytarır
        List<GenreDTO> genres = genreService.getAll();
        return ResponseEntity.ok(genres);
    }

    @PostMapping
    public ResponseEntity<GenreDTO> create(@RequestBody GenreDTO dto) {
        // Service-dəki create metodu artıq dto qəbul edir və dto qaytarır
        GenreDTO savedGenre = genreService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGenre);
    }
}