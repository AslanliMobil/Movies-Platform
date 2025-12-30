package org.example.moviesplatform.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "wishlists")
@Data
public class Wishlist {

    @EmbeddedId
    private WishlistId id; // Composite key obyekti

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // WishlistId daxilindəki userId ilə bağlayır
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId") // WishlistId daxilindəki movieId ilə bağlayır
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();





}
