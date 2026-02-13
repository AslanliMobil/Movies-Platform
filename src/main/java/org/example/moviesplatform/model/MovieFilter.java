package org.example.moviesplatform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieFilter {

    private String title;

    // Buraxılış tarixi aralığı (Məsələn: 2020-01-01 və 2024-12-31 arası)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate releaseDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate releaseDateTo;

    // Reytinq aralığı (Məsələn: 7.0 - 9.5 arası)
    private Double ratingFrom;
    private Double ratingTo;

    // Müddət aralığı (Məsələn: 90 - 150 dəq arası)
    private Integer durationFrom;
    private Integer durationTo;

    // ID-lər vasitəsilə mürəkkəb (Join) süzmə
    private Integer genreId;
    private Integer directorId;
    private Integer actorId; // Bu sahəni də əlavə etdik ki, konkret aktyorun filmlərini tapmaq olsun
}