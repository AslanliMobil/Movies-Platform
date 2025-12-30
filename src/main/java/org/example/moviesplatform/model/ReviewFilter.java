package org.example.moviesplatform.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewFilter {
    private Integer movieId;
    private Integer userId;

    private Integer ratingFrom; // Məsələn: 4 ulduzdan yuxarı rəylər
    private Integer ratingTo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtFrom;
}
