package org.example.moviesplatform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Real mühit üçün Movie Data Transfer Object.
 * Caching üçün Serializable interfeysi tətbiq olunub.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO implements Serializable {

    private static final long serialVersionUID = 1L; // Caching zamanı versiya uyğunsuzluğu olmaması üçün

    private Integer id;

    @NotBlank(message = "Filmin adı boş ola bilməz")
    @Size(max = 200, message = "Filmin adı 200 simvoldan çox olmamalıdır")
    private String title;

    @Size(max = 2000, message = "Təsvir 2000 simvoldan çox olmamalıdır") // 1000 az ola bilər
    private String description;

    @NotNull(message = "Buraxılış tarixi qeyd olunmalıdır")
    @PastOrPresent(message = "Buraxılış tarixi gələcək zaman ola bilməz")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Min(value = 1, message = "Müddət ən az 1 dəqiqə olmalıdır")
    private Integer duration; // dəqiqə ilə

    private String coverImageUrl;

    // MinIO-da saxlanılan video faylının yolu və ya adı
    private String videoUrl;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    private Double averageRating;

    // Əlaqəli obyektlər (DTO-lar daxilində də Serializable olmalıdır)
    @NotEmpty(message = "Ən azı bir janr seçilməlidir")
    private List<GenreDTO> genres;

    @NotEmpty(message = "Aktyor heyəti boş ola bilməz")
    private List<ActorDTO> actors;

    @NotNull(message = "Rejissor qeyd olunmalıdır")
    private DirectorDTO director;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}