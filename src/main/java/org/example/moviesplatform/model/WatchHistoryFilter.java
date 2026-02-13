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
public class WatchHistoryFilter {

    private Integer userId;

    private Integer movieId; // Konkret filmin tarixçəsini yoxlamaq üçün

    @Schema(description = "Film tam bitibmi (90%+ baxılıbmi)?")
    private Boolean isCompleted; // isFinished əvəzinə Entity-yə uyğun ad

    @Schema(description = "Minimum baxış faizi (məs: 50%-dən çox baxılanlar)")
    private Double minProgressPercentage;

    @Schema(description = "Maksimum baxış faizi (məs: hələ 10%-nə baxılanlar)")
    private Double maxProgressPercentage;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastWatchedFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastWatchedTo;

    @Schema(description = "Yalnız çox izlənilən (təkrar baxılan) filmləri gətir")
    private Integer minWatchCount;
}