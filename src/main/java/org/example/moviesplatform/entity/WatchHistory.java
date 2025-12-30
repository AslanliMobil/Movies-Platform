package org.example.moviesplatform.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "watch_history")
@Data
public class WatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(name = "stopped_at")
    private Integer stoppedAt = 0;

    @Column(name = "is_finished")
    private Boolean isFinished = false;

    @Column(name = "last_watched_at")
    private LocalDateTime lastWatchedAt = LocalDateTime.now();
}
