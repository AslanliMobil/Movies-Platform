package org.example.moviesplatform.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WishlistDTO {
    private Integer userId;
    private Integer movieId;
    private String movieTitle;
    private String movieCoverUrl;
    private LocalDateTime createdAt;
}
