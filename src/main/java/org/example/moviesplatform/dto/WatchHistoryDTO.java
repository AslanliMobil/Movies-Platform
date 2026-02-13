package org.example.moviesplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "İzləmə tarixçəsi məlumatları")
public class WatchHistoryDTO {

    private Integer id;

    @NotNull(message = "İstifadəçi ID boş ola bilməz")
    private Integer userId;

    @NotNull(message = "Film ID boş ola bilməz")
    private Integer movieId;

    @Schema(description = "Filmin adı (yalnız cavab üçün)")
    private String movieTitle;

    @Schema(description = "Filmin poster linki (frontend-də siyahı üçün)")
    private String moviePosterUrl;

    @PositiveOrZero(message = "Dayanma saniyəsi mənfi ola bilməz")
    @Schema(description = "İstifadəçinin filmi dayandırdığı saniyə")
    private Long watchedSeconds; // stoppedAt əvəzinə daha aydın ad

    @Schema(description = "Filmin neçə faizinə baxılıb (0-100)")
    private Double progressPercentage;

    @Schema(description = "Film tam bitibmi (adətən >90% olanda true olur)")
    private Boolean isCompleted;

    @Schema(description = "Bu filmə cəmi neçə dəfə baxılıb")
    private Integer watchCount;

    private LocalDateTime lastWatchedAt;
}