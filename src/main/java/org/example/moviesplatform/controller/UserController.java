package org.example.moviesplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.UserDTO;
import org.example.moviesplatform.dto.UserUpdateDTO;
import org.example.moviesplatform.model.UserFilter;
import org.example.moviesplatform.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "İstifadəçi əməliyyatları")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Filtrlə və səhifələmə ilə istifadəçiləri gətir")
    public ResponseEntity<Page<UserDTO>> getAllUsers(UserFilter filter, Pageable pageable) {
        log.info("Siyahı sorğusu: filtr={}, page={}", filter, pageable.getPageNumber());
        return ResponseEntity.ok(userService.getAllUsers(filter, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID-yə görə istifadəçini gətir")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        log.info("Tək istifadəçi sorğusu: id={}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @Operation(summary = "Yeni istifadəçi qeydiyyatı")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Məlumatları qismən yenilə")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateDTO dto) {
        log.info("Yenilənmə: id={}", id);
        return ResponseEntity.ok(userService.updateUserPartial(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "İstifadəçini sil (Soft-delete)")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Silinmiş istifadəçini bərpa et")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> restoreUser(@PathVariable Integer id) {
        log.info("Bərpa sorğusu: id={}", id);
        return ResponseEntity.ok(userService.restoreUser(id));
    }
}