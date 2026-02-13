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
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    /**
     * 1. DİNAMİK AXtARIŞ VƏ FİLTRASİYA
     * Məqsəd: Verilən kriteriyalara (ad, bioqrafiya, doğum/ölüm tarixi) uyğun rejissorları tapmaq.
     * Metod: GET
     * URL Nümunəsi: /directors/search?name=Nolan&birthDateFrom=1970-01-01
     */
    @GetMapping("/search")
    public ResponseEntity<List<DirectorDTO>> search(DirectorFilter filter) {
        return ResponseEntity.ok(directorService.search(filter));
    }

    /**
     * 2. BÜTÜN REJİSSORLARI LİSTƏLƏMƏK
     * Məqsəd: Bazadakı bütün rejissorları heç bir filtr olmadan gətirmək.
     * Metod: GET
     * URL Nümunəsi: /directors
     */
    @GetMapping
    public ResponseEntity<List<DirectorDTO>> getAll() {
        return ResponseEntity.ok(directorService.getAllDirectors());
    }

    /**
     * 3. ID İLƏ TƏK REJİSSORU TAPMAQ
     * Məqsəd: Spessifik bir ID-yə sahib rejissorun detallarını görmək.
     * Metod: GET
     * URL Nümunəsi: /directors/5
     */
    @GetMapping("/{id}")
    public ResponseEntity<DirectorDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(directorService.getDirectorById(id));
    }

    /**
     * 4. YENİ REJİSSOR YARATMAQ
     * Məqsəd: Bazaya yeni rejissor əlavə etmək (Adın unikal olması və tarix yoxlaması burada işləyir).
     * Metod: POST
     * Body: JSON formatında DirectorDTO
     */
    @PostMapping
    public ResponseEntity<DirectorDTO> create(@RequestBody DirectorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(directorService.createDirector(dto));
    }

    /**
     * 5. TAM YENİLƏMƏ (FULL UPDATE)
     * Məqsəd: Mövcud rejissorun BÜTÜN sahələrini dəyişmək. Göndərilməyən sahələr null ola bilər.
     * Metod: PUT
     * URL Nümunəsi: /directors/1
     */
    @PutMapping("/{id}")
    public ResponseEntity<DirectorDTO> update(@PathVariable Integer id, @RequestBody DirectorDTO dto) {
        return ResponseEntity.ok(directorService.updateDirector(id, dto));
    }

    /**
     * 6. QİSMİ YENİLƏMƏ (PARTIAL UPDATE)
     * Məqsəd: Rejissorun yalnız istənilən sahəsini (məsələn, yalnız bioqrafiyanı) digərlərinə toxunmadan dəyişmək.
     * Metod: PATCH
     * URL Nümunəsi: /directors/1
     */
    @PatchMapping("/{id}")
    public ResponseEntity<DirectorDTO> patch(@PathVariable Integer id, @RequestBody DirectorDTO dto) {
        return ResponseEntity.ok(directorService.patchDirector(id, dto));
    }

    /**
     * 7. REJİSSORU SİLMƏK
     * Məqsəd: Verilmiş ID-yə sahib rejissoru bazadan tamamilə silmək.
     * Metod: DELETE
     * URL Nümunəsi: /directors/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        directorService.deleteDirector(id);
        return ResponseEntity.noContent().build();
    }
}