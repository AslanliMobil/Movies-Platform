package org.example.moviesplatform.security.model.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserPermission {
    MOVIE_READ("movies:read"),
    MOVIE_WRITE("movies:write"),
    WISHLIST_READ("wishlist:read"),
    WISHLIST_WRITE("wishlist:write");

    private final String permission;
}
