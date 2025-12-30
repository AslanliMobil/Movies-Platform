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
public class RoleFilter {

    private String name; // Məsələn: "ADMIN" yazanda ROLE_ADMIN çıxsın

    @Size(max = 100)
    private String description;
}
