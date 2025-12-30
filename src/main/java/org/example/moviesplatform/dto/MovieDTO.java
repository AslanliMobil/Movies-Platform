package org.example.moviesplatform.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieDTO {
    private Integer id;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private String coverImageUrl;
    private Double averageRating;

    // Əlaqəli obyektlərin siyahısı
    private List<GenreDTO> genres;
    private List<ActorDTO> actors;
    private List<DirectorDTO> directors;

    private LocalDateTime createdAt;
}
