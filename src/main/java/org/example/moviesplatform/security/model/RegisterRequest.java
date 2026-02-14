package org.example.moviesplatform.security.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.moviesplatform.Validation.ValidPassword;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "İstifadəçi adı boş ola bilməz")
    private String username;

    // Standart @Size əvəzinə müəllimin @ValidPassword-unu yazırıq
    @ValidPassword
    private String password;

    @NotBlank(message = "Email boş ola bilməz")
    @Email(message = "Düzgün email formatı daxil edin")
    private String email;

    @NotBlank(message = "Ad boş ola bilməz")
    private String firstName;

    @NotBlank(message = "Soyad boş ola bilməz")
    private String lastName;
}