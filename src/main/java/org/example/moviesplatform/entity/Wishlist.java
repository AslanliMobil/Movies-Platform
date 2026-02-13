package org.example.moviesplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import org.example.moviesplatform.security.repository.entity.UserEntity;

@Entity
@Table(name = "wishlists")
@Data
@NoArgsConstructor
public class Wishlist {

    @EmbeddedId
    private WishlistId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // WishlistId daxilindəki userId-ni bu əlaqə ilə doldurur
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_wishlist_user"))
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId") // WishlistId daxilindəki movieId-ni bu əlaqə ilə doldurur
    @JoinColumn(name = "movie_id", foreignKey = @ForeignKey(name = "fk_wishlist_movie"))
    private Movie movie;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Entity yaradılarkən avtomatik tarix qoyulması üçün
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}