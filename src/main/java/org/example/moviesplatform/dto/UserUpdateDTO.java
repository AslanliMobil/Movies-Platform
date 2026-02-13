package org.example.moviesplatform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @Size(min = 3, max = 20)
    private String username;

    private String firstName;
    private String lastName;

    @Email
    private String email;

    @Size(min = 8)
    private String password;
}
