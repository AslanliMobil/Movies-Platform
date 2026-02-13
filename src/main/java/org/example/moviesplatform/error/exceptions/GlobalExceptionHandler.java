package org.example.moviesplatform.error.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.error.model.ErrorResponse;
import org.example.moviesplatform.error.model.MovieNotFoundException;
import org.example.moviesplatform.error.model.UserNotFoundException;
import org.example.moviesplatform.error.model.WishlistNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice // ControllerAdvice-in daha müasir versiyası
public class GlobalExceptionHandler {

    // 1. VALIDATION XƏTALARI (@Valid ilə gələn)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        log.warn("Validation failed: {}", errors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", "Daxil edilən məlumatlar qaydalara uyğun deyil.", errors);
    }

    // 2. FORBIDDEN (403 - Giriş rədd edildi)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        log.error("Access denied error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Forbidden", "Bu əməliyyat üçün kifayət qədər səlahiyyətiniz yoxdur.", null);
    }

    // 3. UNAUTHORIZED (401 - Login xətası)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthenticationException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "İstifadəçi kimliyi təsdiq olunmadı.", null);
    }

    // 4. NOT FOUND (404)
    @ExceptionHandler({
            UserNotFoundException.class, MovieNotFoundException.class,
            WishlistNotFoundException.class // Digər NotFound-ları bura əlavə et
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), null);
    }

    // 5. DATABASE XƏTALARI (500)
    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseException(org.springframework.dao.DataAccessException ex) {
        log.error("Database connection error: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", "Verilənlər bazası ilə əlaqə zamanı daxili xəta baş verdi.", null);
    }

    // 6. GENERAL EXCEPTION (Bütün digər gözlənilməyən xətalar)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        log.error("Unexpected error occurred: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Sistemdə gözlənilməyən xəta baş verdi.", null);
    }

    // Köməkçi metod (Refactored)
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String error, String message, List<String> validationErrors) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .status(status.value())
                        .error(error)
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .validationErrors(validationErrors)
                        .build()
        );
    }
}