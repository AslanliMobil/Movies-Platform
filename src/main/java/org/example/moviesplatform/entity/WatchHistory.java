package org.example.moviesplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.example.moviesplatform.security.repository.entity.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "watch_history", uniqueConstraints = {
        // Bir istifadəçinin bir film üçün yalnız bir tarixçə qeydi ola bilər
        @UniqueConstraint(columnNames = {"user_id", "movie_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "watched_seconds", nullable = false)
    private Long watchedSeconds = 0L; // stopped_at əvəzinə daha texniki ad

    @Column(name = "progress_percentage")
    private Double progressPercentage = 0.0; // 0.0 - 100.0 arası

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false; // isFinished əvəzinə

    @Column(name = "watch_count")
    private Integer watchCount = 1; // İstifadəçinin bu filmə neçə dəfə kliklədiyini izləmək üçün

    @UpdateTimestamp
    @Column(name = "last_watched_at")
    private LocalDateTime lastWatchedAt;

    @CreationTimestamp
    @Column(name = "first_watched_at", updatable = false)
    private LocalDateTime firstWatchedAt;

    // Köməkçi metod: Proqresi hesablamaq üçün
    public void calculateProgress(Integer movieDurationInMinutes) {
        if (movieDurationInMinutes == null || movieDurationInMinutes == 0) return;

        long totalSeconds = movieDurationInMinutes * 60L;
        this.progressPercentage = (double) (this.watchedSeconds * 100) / totalSeconds;

        if (this.progressPercentage > 100.0) this.progressPercentage = 100.0;

        // Əgər 90%-dən çoxuna baxıbsa, avtomatik tamamlanmış sayılır
        this.isCompleted = this.progressPercentage >= 90.0;
    }
}