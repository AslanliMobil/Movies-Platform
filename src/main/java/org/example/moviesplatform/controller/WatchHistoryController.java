package org.example.moviesplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.WatchHistoryDTO;
import org.example.moviesplatform.service.WatchHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watch-history")
@RequiredArgsConstructor
@Tag(name = "Watch History", description = "İzləmə tarixçəsi və 'Davam et' məntiqi")
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "İstifadəçinin bütün izləmə tarixçəsi (Səhifəli)")
    public ResponseEntity<Page<WatchHistoryDTO>> getUserHistory(
            @PathVariable Integer userId,
            Pageable pageable) {
        // DÜZƏLİŞ: getIHistoryByUserId -> getHistoryByUserId (artıq 'I' silindi)
        return ResponseEntity.ok(watchHistoryService.getHistoryByUserId(userId, pageable));
    }

    @GetMapping("/user/{userId}/continue")
    @Operation(summary = "Yarımçıq qalmış filmlər (Continue Watching)")
    public ResponseEntity<List<WatchHistoryDTO>> getContinueWatching(@PathVariable Integer userId) {
        // DÜZƏLİŞ: Service-də bu metodun olduğundan əmin ol
        return ResponseEntity.ok(watchHistoryService.getIncompleteHistory(userId));
    }

    @PatchMapping("/progress")
    @Operation(summary = "İzləmə proqresini yenilə (Saniyə ilə)")
    public ResponseEntity<WatchHistoryDTO> updateProgress(@RequestBody WatchHistoryDTO dto) {
        return ResponseEntity.ok(watchHistoryService.saveOrUpdateHistory(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFromHistory(@PathVariable Long id) {
        // DÜZƏLİŞ: Entity-də ID Long olduğu üçün burada da Long etdik
        watchHistoryService.delete(id);
    }

    @DeleteMapping("/user/{userId}/clear")
    @Operation(summary = "Bütün tarixçəni təmizlə")
    public ResponseEntity<Void> clearAllHistory(@PathVariable Integer userId) {
        // DÜZƏLİŞ: Service-də metod adını buna uyğunlaşdır (clearHistoryByUserId)
        watchHistoryService.clearHistoryByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}