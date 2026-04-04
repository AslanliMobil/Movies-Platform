package org.example.moviesplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.ActorDTO;
import org.example.moviesplatform.model.ActorFilter; // Aktyor üçün filter klassı yaratmalısınız
import org.example.moviesplatform.service.ActorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/actors")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    @GetMapping("/search")
    public ResponseEntity<List<ActorDTO>> search(ActorFilter filter) {
        return ResponseEntity.ok(actorService.search(filter));
    }

    @GetMapping
    public ResponseEntity<List<ActorDTO>> getAll() {
        return ResponseEntity.ok(actorService.getAllActors());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ActorDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(actorService.getActorById(id));
    }


    @PostMapping
    public ResponseEntity<ActorDTO> create(@RequestBody ActorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(actorService.createActor(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ActorDTO> update(@PathVariable Integer id, @RequestBody ActorDTO dto) {
        return ResponseEntity.ok(actorService.updateActor(id, dto));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ActorDTO> patch(@PathVariable Integer id, @RequestBody ActorDTO dto) {
        return ResponseEntity.ok(actorService.patchActor(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        actorService.deleteActor(id);
        return ResponseEntity.noContent().build();
    }
}