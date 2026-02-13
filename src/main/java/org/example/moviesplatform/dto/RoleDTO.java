package org.example.moviesplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Integer id;

    @NotBlank(message = "Rol adı boş ola bilməz")
    @Size(min = 3, max = 50, message = "Rol adı 3-50 simvol aralığında olmalıdır")
    @Pattern(regexp = "^ROLE_[A-Z_]+$", message = "Rol adı 'ROLE_' ilə başlamalı və böyük hərflərlə olmalıdır")
    private String name; // Məsələn: ROLE_USER, ROLE_ADMIN

    @Size(max = 255, message = "Açıqlama 255 simvoldan çox ola bilməz")
    private String description;
}