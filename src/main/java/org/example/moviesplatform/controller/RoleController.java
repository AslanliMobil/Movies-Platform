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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GetMapping
    public ResponseEntity<Page<RoleDTO>> getAllRoles(RoleFilter filter, Pageable pageable) {
        return ResponseEntity.ok(roleService.getAllRoles(filter, pageable)
                .map(roleMapper::toDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(roleMapper.toDTO(roleService.getRoleById(id)));
    }

    @GetMapping("/by-name")
    public ResponseEntity<RoleDTO> getByName(@RequestParam String name) {
        return ResponseEntity.ok(roleMapper.toDTO(roleService.getByName(name)));
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        var role = roleMapper.toEntity(roleDTO);
        var savedRole = roleService.addRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleMapper.toDTO(savedRole));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoleDTO> partialUpdate(@PathVariable Integer id, @RequestBody RoleDTO roleDTO) {
        var rolePayload = roleMapper.toEntity(roleDTO);
        var updatedRole = roleService.update(id, rolePayload);
        return ResponseEntity.ok(roleMapper.toDTO(updatedRole));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}