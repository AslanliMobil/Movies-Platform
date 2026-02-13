package org.example.moviesplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "İstək siyahısı məlumatları")
public class WishlistDTO {

    @NotNull(message = "İstifadəçi ID-si boş ola bilməz")
    @Schema(example = "1")
    private Integer userId;

    @NotNull(message = "Film ID-si boş ola bilməz")
    @Schema(example = "105")
    private Integer movieId;

    // Response zamanı görünəcək detallar (ReadOnly)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Filmin adı", example = "Inception")
    private String movieTitle;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Filmin afişası", example = "https://cdn.example.com/posters/inception.jpg")
    private String movieCoverUrl;

    @Schema(description = "İstifadəçinin bu film haqqında xüsusi qeydi", example = "Haftasonu ailə ilə baxılacaq")
    private String note;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Siyahıya əlavə edilmə tarixi")
    private LocalDateTime createdAt;
}