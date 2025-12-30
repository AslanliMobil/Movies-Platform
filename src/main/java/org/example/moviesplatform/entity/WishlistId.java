package org.example.moviesplatform.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class WishlistId implements java.io.Serializable {
    private Integer userId;
    private Integer movieId;
}
