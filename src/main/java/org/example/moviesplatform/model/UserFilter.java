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
@Schema(description = "İstifadəçi axtarışı üçün filtr parametrləri")
public class UserFilter {

    @Schema(description = "İstifadəçi adı (qismən axtarış dəstəklənir)", example = "admin")
    private String username;

    @Schema(description = "Email ünvanı (qismən axtarış dəstəklənir)", example = "example@com")
    private String email;

    @Schema(description = "Ad", example = "Elvin")
    private String firstName;

    @Schema(description = "Soyad", example = "Məmmədov")
    private String lastName;

    @Schema(description = "Axtarış üçün başlanğıc tarixi (ISO format: yyyy-MM-dd'T'HH:mm:ss)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtFrom;

    @Schema(description = "Axtarış üçün son tarix")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtTo;

    @Schema(description = "Silinmiş istifadəçiləri də daxil etmək üçün (default: false)", example = "false")
    private Boolean includeDeleted = false; // Real layihədə adminlər silinənləri də görmək istəyə bilər
}