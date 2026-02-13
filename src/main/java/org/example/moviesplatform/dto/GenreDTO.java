package org.example.moviesplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotBlank(message = "Janr adı boş ola bilməz")
    @Size(min = 2, max = 50, message = "Janr adı 2 ilə 50 simvol arasında olmalıdır")
    private String name;
}