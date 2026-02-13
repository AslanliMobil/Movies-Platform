package org.example.moviesplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.GenreDTO;
import org.example.moviesplatform.model.GenreFilter; // Filter klassını import et
import org.example.moviesplatform.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    /**
     * 1. DİNAMİK AXTARIŞ (SEARCH)
     * VACİB: Bu metod @GetMapping("/{id}")-dən yuxarıda olmalıdır!
     * Məqsəd: Ad və ya tarixə görə süzgəcdən keçirmək.
     */
    @GetMapping("/search")
    public ResponseEntity<List<GenreDTO>> search(GenreFilter filter) {
        return ResponseEntity.ok(genreService.search(filter));
    }

    /**
     * 2. BÜTÜN JANRLARI LİSTƏLƏMƏK
     */
    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAll() {
        return ResponseEntity.ok(genreService.getAll());
    }

    /**
     * 3. ID-YƏ GÖRƏ JANR TAPMAQ
     */
    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(genreService.getById(id));
    }

    /**
     * 4. YENİ JANR YARATMAQ
     */
    @PostMapping
    public ResponseEntity<GenreDTO> create(@RequestBody GenreDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.create(dto));
    }

    /**
     * 5. JANRI YENİLƏMƏK (PUT)
     */
    @PutMapping("/{id}")
    public ResponseEntity<GenreDTO> update(@PathVariable Integer id, @RequestBody GenreDTO dto) {
        return ResponseEntity.ok(genreService.update(id, dto));
    }

    /**
     * 6. JANRI SİLMƏK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        genreService.delete(id); // Sadəcə silirik

        Map<String, String> response = new HashMap<>();
        response.put("message", "Janr sistemdən uğurla silindi."); // ID-siz, təmiz mesaj

        return ResponseEntity.ok(response);
    }
}