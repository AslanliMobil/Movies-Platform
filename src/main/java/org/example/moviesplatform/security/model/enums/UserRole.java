package org.example.moviesplatform.security.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserRole {

    USER(Set.of(
            UserPermission.MOVIE_READ,
            UserPermission.WISHLIST_READ,
            UserPermission.WISHLIST_WRITE
    )),

    ADMIN(Set.of(
            UserPermission.MOVIE_READ,
            UserPermission.MOVIE_WRITE,
            UserPermission.WISHLIST_READ,
            UserPermission.WISHLIST_WRITE
    ));

    private final Set<UserPermission> permissions;

    /**
     * Bu metod həm "movies:read" tipli icazələri,
     * həm də "ROLE_ADMIN" tipli rolları tək bir listə toplayır.
     */
    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}