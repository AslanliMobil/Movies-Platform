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

    @Size(max = 50, message = "Axtarış üçün rol adı çox uzundur")
    private String name; // Məsələn: "user" yazanda ROLE_USER gəlsin (ignore case)

    @Size(max = 100, message = "Açıqlama axtarışı üçün limit 100 simvoldur")
    private String description;

    // Gələcəkdə tarixlə axtarış üçün bura createdAtFrom və createdAtTo əlavə edilə bilər
}