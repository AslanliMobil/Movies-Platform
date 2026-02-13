package org.example.moviesplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.WishlistDTO;
import org.example.moviesplatform.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')") // Təhlükəsizlik: Yalnız öz siyahısına baxa bilər
    public ResponseEntity<List<WishlistDTO>> getUserWishlist(@PathVariable Integer userId) {
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<WishlistDTO> addToWishlist(@RequestBody WishlistDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistService.add(dto));
    }

    @DeleteMapping("/user/{userId}/movie/{movieId}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Integer userId, @PathVariable Integer movieId) {
        wishlistService.remove(userId, movieId);
        return ResponseEntity.noContent().build();
    }
}