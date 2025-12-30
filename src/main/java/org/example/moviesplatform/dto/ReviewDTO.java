package org.example.moviesplatform.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Integer id;
    private Integer userId;     // Bütün User obyektini yox, sadəcə ID-sini ötürmək daha yüngüldür
    private String username;    // İstifadəçinin adını göstərmək üçün
    private Integer movieId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
