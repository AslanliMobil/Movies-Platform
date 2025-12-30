package org.example.moviesplatform.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WatchHistoryDTO {
    private Integer id;
    private Integer userId;     // Bu sahə mütləq olmalıdır
    private Integer movieId;    // Bu sahə mütləq olmalıdır
    private String movieTitle;
    private Integer stoppedAt;
    private Boolean isFinished;
    private LocalDateTime lastWatchedAt;
}