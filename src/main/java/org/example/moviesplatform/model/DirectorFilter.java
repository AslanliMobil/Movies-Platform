package org.example.moviesplatform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectorFilter {

    private String name;

    private Integer birthYearFrom;
    private Integer birthYearTo;

    // Rejissorun ən azı neçə filmi olduğunu süzmək üçün (isteğe bağlı)
    private Integer minMovieCount;
}
