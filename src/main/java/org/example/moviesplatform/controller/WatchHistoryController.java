package org.example.moviesplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.WatchHistoryDTO;
import org.example.moviesplatform.service.WatchHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watch-history")
@RequiredArgsConstructor
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    // İstifadəçinin baxdığı filmlər (Son izlənilənə görə sıralanmış)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WatchHistoryDTO>> getUserHistory(@PathVariable Integer userId) {
        return ResponseEntity.ok(watchHistoryService.getHistoryByUserId(userId));
    }

    // Tarixçəni yeniləmək və ya yeni baxış qeydi yaratmaq
    // Məsələn: Filmi yarıda qoyanda stopped_at saniyəsini göndəririk
    @PostMapping("/update")
    public ResponseEntity<WatchHistoryDTO> updateProgress(@RequestBody WatchHistoryDTO dto) {
        return ResponseEntity.ok(watchHistoryService.saveOrUpdateHistory(dto));
    }

    // Tarixçədən bir filmi silmək
    // WatchHistoryController.java daxilində
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFromHistory(@PathVariable Integer id) {
        // İndi Service-də 'delete' metodu olduğu üçün bu sətir xəta verməyəcək
        watchHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
