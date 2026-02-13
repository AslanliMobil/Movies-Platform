package org.example.moviesplatform.error.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Null olan sahələri (məs: validationErrors) gizlədir
public class ErrorResponse {
    private String error;
    private String message;
    private int status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private List<String> validationErrors; // Yalnız validasiya xətaları olduqda görünəcək
}
