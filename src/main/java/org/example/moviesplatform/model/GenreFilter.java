package org.example.moviesplatform.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreFilter {

    @Size(max = 30, message = "Genre name search must be maximum 30 characters")
    private String name;

    // Bu janrda ən azı neçə film olmasını süzmək üçün (məsələn: boş janrları göstərmə)
    private Integer minMovieCount;
}