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
public class ActorFilter {

    // Aktyorun adına görə axtarış
    private String name;

    // Aktyorun bioqrafiyası daxilində söz axtarışı
    private String biography;

    // Doğum tarixi aralığı (Məsələn: 1980-01-01 və 1990-12-31 arası doğulanlar)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDateTo;

    // Ölüm tarixi aralığı
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deathDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deathDateTo;
}