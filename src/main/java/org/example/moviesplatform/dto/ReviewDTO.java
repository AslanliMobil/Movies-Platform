package org.example.moviesplatform.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Integer id;

    @NotNull(message = "İstifadəçi ID-si mütləqdir")
    private Integer userId;

    // Username adətən ancaq "Response" (cavab) zamanı dolur,
    // rəy yaradanda bunu göndərməyə ehtiyac yoxdur.
    private String username;

    @NotNull(message = "Film ID-si mütləqdir")
    private Integer movieId;

    @NotNull(message = "Reytinq boş ola bilməz")
    @Min(value = 1, message = "Reytinq minimum 1 ola bilər")
    @Max(value = 10, message = "Reytinq maksimum 10 ola bilər")
    private Double rating;

    @NotBlank(message = "Rəy mətni (comment) boş ola bilməz")
    @Size(min = 10, max = 500, message = "Rəy mətni 10-500 simvol aralığında olmalıdır")
    private String comment;

    private LocalDateTime createdAt;
}