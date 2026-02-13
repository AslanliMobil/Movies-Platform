package org.example.moviesplatform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreFilter {

    // Janrın adına görə axtarış (Məs: "Act" yazanda Action tapılsın)
    private String name;

    // Yaradılma tarixinə görə filtr (Hansı tarixdən sonra yaradılıb?)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtFrom;

    // Hansı tarixdən əvvəl yaradılıb?
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtTo;

    // Bu janrda ən azı neçə film olmasını süzmək üçün
    private Integer minMovieCount;
}