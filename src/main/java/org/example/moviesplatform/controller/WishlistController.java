package org.example.moviesplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.WishlistDTO;
import org.example.moviesplatform.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // Müəyyən bir istifadəçinin bütün istək siyahısı
    // GET /wishlists/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishlistDTO>> getUserWishlist(@PathVariable Integer userId) {
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }

    // Siyahıya film əlavə etmək
    @PostMapping
    public ResponseEntity<WishlistDTO> addToWishlist(@RequestBody WishlistDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistService.add(dto));
    }

    // Siyahıdan film silmək
    @DeleteMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Integer userId, @PathVariable Integer movieId) {
        wishlistService.remove(userId, movieId);
        return ResponseEntity.noContent().build();
    }
}
