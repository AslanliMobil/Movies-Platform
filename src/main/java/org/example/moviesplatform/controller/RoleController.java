package org.example.moviesplatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.RoleDTO;
import org.example.moviesplatform.mapper.RoleMapper;
import org.example.moviesplatform.model.RoleFilter;
import org.example.moviesplatform.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.groups.Default;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RoleDTO>> getAllRoles(RoleFilter filter, Pageable pageable) {
        return ResponseEntity.ok(roleService.getAllRoles(filter, pageable)
                .map(roleMapper::toDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(roleMapper.toDTO(roleService.getRoleById(id)));
    }

    @GetMapping("/by-name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> getByName(@RequestParam String name) {
        return ResponseEntity.ok(roleMapper.toDTO(roleService.getByName(name)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> createRole(@Validated({RoleDTO.Create.class, Default.class}) @RequestBody RoleDTO roleDTO) {
        var role = roleMapper.toEntity(roleDTO);
        var savedRole = roleService.addRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleMapper.toDTO(savedRole));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> partialUpdate(@PathVariable Integer id, @Valid @RequestBody RoleDTO roleDTO) {
        var rolePayload = roleMapper.toEntity(roleDTO);
        var updatedRole = roleService.update(id, rolePayload);
        return ResponseEntity.ok(roleMapper.toDTO(updatedRole));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}