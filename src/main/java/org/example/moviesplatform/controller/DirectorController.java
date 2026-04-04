package org.example.moviesplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.DirectorDTO;
import org.example.moviesplatform.model.DirectorFilter;
import org.example.moviesplatform.service.DirectorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;


    @GetMapping("/search")
    public ResponseEntity<List<DirectorDTO>> search(DirectorFilter filter) {
        return ResponseEntity.ok(directorService.search(filter));
    }


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


    @PutMapping("/{id}")
    public ResponseEntity<DirectorDTO> update(@PathVariable Integer id, @RequestBody DirectorDTO dto) {
        return ResponseEntity.ok(directorService.updateDirector(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DirectorDTO> patch(@PathVariable Integer id, @RequestBody DirectorDTO dto) {
        return ResponseEntity.ok(directorService.patchDirector(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        directorService.deleteDirector(id);
        return ResponseEntity.noContent().build();
    }
}