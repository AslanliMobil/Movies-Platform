package org.example.moviesplatform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotBlank(message = "Rejissor adı boş ola bilməz")
    @Size(max = 100, message = "Ad 100 simvoldan çox olmamalıdır")
    private String name;

    @Size(max = 5000, message = "Bioqrafiya 5000 simvoldan çox olmamalıdır")
    private String biography;

    @Past(message = "Doğum tarixi keçmiş zaman olmalıdır")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deathDate;
}