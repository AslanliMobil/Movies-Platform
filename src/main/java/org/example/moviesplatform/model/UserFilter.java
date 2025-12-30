package org.example.moviesplatform.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UserFilter {

    @Size(max = 50, message = "Username must be maximum 50 characters")
    private String username;

    @Email(message = "Please enter a valid email address")
    private String email;

    @Size(max = 50, message = "First name must be maximum 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be maximum 50 characters")
    private String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtTo;
}