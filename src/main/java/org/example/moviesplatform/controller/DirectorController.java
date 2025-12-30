package org.example.moviesplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.DirectorDTO;
import org.example.moviesplatform.mapper.DirectorMapper;
import org.example.moviesplatform.service.DirectorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;
    private final DirectorMapper directorMapper;

    @GetMapping
    public ResponseEntity<List<DirectorDTO>> getAll() {
        return ResponseEntity.ok(directorService.getAllDirectors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectorDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(directorService.getDirectorById(id));
    }

    @PostMapping
    public ResponseEntity<DirectorDTO> create(@RequestBody DirectorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(directorService.createDirector(dto));
    }
}
