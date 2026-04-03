package org.example.moviesplatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.GenreDTO;
import org.example.moviesplatform.model.GenreFilter;
import org.example.moviesplatform.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/search")
    public ResponseEntity<List<GenreDTO>> search(GenreFilter filter) {
        return ResponseEntity.ok(genreService.search(filter));
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAll() {
        return ResponseEntity.ok(genreService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(genreService.getById(id));
    }

    @PostMapping
    public ResponseEntity<GenreDTO> create(@Valid @RequestBody GenreDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDTO> update(@PathVariable Integer id, @Valid @RequestBody GenreDTO dto) {
        return ResponseEntity.ok(genreService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        genreService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Janr sistemdən uğurla silindi.");
        return ResponseEntity.ok(response);
    }
}