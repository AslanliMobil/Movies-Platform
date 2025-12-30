package org.example.moviesplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Yalnız yazmaq üçündür (POST/PUT)
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
