package org.example.moviesplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.WishlistDTO;
import org.example.moviesplatform.model.WishlistFilter;
import org.example.moviesplatform.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlists")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "İstifadəçinin izləmək istədiyi filmlər")
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WishlistDTO>> getUserWishlist(@PathVariable Integer userId) {
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }

    @GetMapping("/search")
    @Operation(summary = "İstək siyahısında filtrlə axtarış")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WishlistDTO>> searchWishlist(WishlistFilter filter) {
        return ResponseEntity.ok(wishlistService.search(filter));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<WishlistDTO> addToWishlist(@Valid @RequestBody WishlistDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistService.add(dto));
    }

    @DeleteMapping("/user/{userId}/movie/{movieId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Integer userId, @PathVariable Integer movieId) {
        wishlistService.remove(userId, movieId);
        return ResponseEntity.noContent().build();
    }
}