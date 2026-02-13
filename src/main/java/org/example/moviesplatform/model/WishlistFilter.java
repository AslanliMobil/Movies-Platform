package org.example.moviesplatform.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "İstək siyahısını filtrləmək üçün kriteriyalar")
public class WishlistFilter {

    @Schema(description = "İstifadəçinin ID-si", example = "1")
    private Integer userId;

    @Schema(description = "Spesifik film ID-si", example = "10")
    private Integer movieId;

    @Schema(description = "Film adının bir hissəsi (Axtarış üçün)", example = "Inc")
    private String movieTitle;

    @Schema(description = "Janr ID-si üzrə filtr", example = "5")
    private Integer genreId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Bu tarixdən sonra əlavə edilənlər")
    private LocalDateTime addedFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Bu tarixə qədər əlavə edilənlər")
    private LocalDateTime addedTo;

    @Schema(description = "Yalnız qeydi olanları gətir (true/false)")
    private Boolean hasNote;
}