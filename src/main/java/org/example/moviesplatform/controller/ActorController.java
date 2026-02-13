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
@RequestMapping("/actors")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    /**
     * 1. DİNAMİK AXtARIŞ VƏ FİLTRASİYA
     * Məqsəd: Ad, bioqrafiya və ya tarix aralığına görə aktyorları tapmaq.
     * Metod: GET
     * URL Nümunəsi: /actors/search?name=DiCaprio
     */
    @GetMapping("/search")
    public ResponseEntity<List<ActorDTO>> search(ActorFilter filter) {
        return ResponseEntity.ok(actorService.search(filter));
    }

    /**
     * 2. BÜTÜN AKTYORLARI LİSTƏLƏMƏK
     * Məqsəd: Bazadakı bütün aktyor qeydlərini gətirmək.
     * Metod: GET
     */
    @GetMapping
    public ResponseEntity<List<ActorDTO>> getAll() {
        return ResponseEntity.ok(actorService.getAllActors());
    }

    /**
     * 3. ID İLƏ TƏK AKTYORU TAPMAQ
     * Məqsəd: Müəyyən bir ID-yə sahib aktyorun profili ilə tanış olmaq.
     * Metod: GET
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActorDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(actorService.getActorById(id));
    }

    /**
     * 4. YENİ AKTYOR YARATMAQ
     * Məqsəd: Sistemə yeni bir aktyor əlavə etmək.
     * Metod: POST
     */
    @PostMapping
    public ResponseEntity<ActorDTO> create(@RequestBody ActorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(actorService.createActor(dto));
    }

    /**
     * 5. TAM YENİLƏMƏ (FULL UPDATE)
     * Məqsəd: Aktyorun bütün məlumatlarını (ad, bio, tarixlər) yenisi ilə əvəz etmək.
     * Metod: PUT
     */
    @PutMapping("/{id}")
    public ResponseEntity<ActorDTO> update(@PathVariable Integer id, @RequestBody ActorDTO dto) {
        return ResponseEntity.ok(actorService.updateActor(id, dto));
    }

    /**
     * 6. QİSMİ YENİLƏMƏ (PARTIAL UPDATE)
     * Məqsəd: Aktyorun yalnız dəyişən sahələrini (məs: sadəcə bioqrafiya) yeniləmək.
     * Metod: PATCH
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ActorDTO> patch(@PathVariable Integer id, @RequestBody ActorDTO dto) {
        return ResponseEntity.ok(actorService.patchActor(id, dto));
    }

    /**
     * 7. AKTYORU SİLMƏK
     * Məqsəd: Aktyor qeydini sistemdən tamamilə silmək.
     * Metod: DELETE
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        actorService.deleteActor(id);
        return ResponseEntity.noContent().build();
    }
}