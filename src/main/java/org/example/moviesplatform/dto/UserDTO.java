package org.example.moviesplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "İstifadəçi məlumat transfer obyekti")
public class UserDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "İstifadəçi adı boş ola bilməz")
    @Size(min = 3, max = 20, message = "İstifadəçi adı 3-20 simvol aralığında olmalıdır")
    @Schema(example = "elvin_99")
    private String username;

    @NotBlank(message = "Ad boş ola bilməz")
    @Schema(example = "Elvin")
    private String firstName;

    @NotBlank(message = "Soyad boş ola bilməz")
    @Schema(example = "Məmmədov")
    private String lastName;

    @Email(message = "Email formatı düzgün deyil")
    @NotBlank(message = "Email boş ola bilməz")
    @Schema(example = "elvin@example.com")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Parol boş ola bilməz")
    @Size(min = 8, message = "Parol ən azı 8 simvol olmalıdır")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
            message = "Parolda ən azı bir rəqəm, bir kiçik və bir böyük hərf olmalıdır")
    @Schema(description = "Yalnız qeydiyyat və yeniləmə zamanı istifadə olunur", example = "P@ssword123")
    private String password;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}