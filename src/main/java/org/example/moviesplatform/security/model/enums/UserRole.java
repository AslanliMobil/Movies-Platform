package org.example.moviesplatform.security.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

// Əgər UserPermission fərqli paketdədirsə, onu import etməyi unutma
// import org.example.moviesplatform.model.enums.UserPermission;

@Getter
@AllArgsConstructor
public enum UserRole {

    // USER: Oxuya bilər və öz wishlist-ini idarə edə bilər
    USER(Set.of(
            UserPermission.MOVIE_READ,
            UserPermission.WISHLIST_READ,
            UserPermission.WISHLIST_WRITE
    )),

    // ADMIN: Hər şeyi edə bilər (Filmləri silmək və əlavə etmək daxil)
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
        // 1. İcazələri (Permissions) çeviririk
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());

        // 2. Rolun özünü "ROLE_" prefiksi ilə əlavə edirik
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}