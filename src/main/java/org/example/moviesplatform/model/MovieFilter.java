package org.example.moviesplatform.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieFilter {
    private String title;

    private Integer releaseYear;

    // Reytinq aralığı (məsələn: 7.0 və 9.5 arası filmlər)
    private Double ratingFrom;
    private Double ratingTo;

    // Müddət aralığı (məsələn: 90 dəq və 150 dəq arası)
    private Integer durationFrom;
    private Integer durationTo;

    // ID-lər vasitəsilə süzmək
    private Integer genreId;
    private Integer directorId;
}
