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
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    // 1. GET ALL (Filter + Pagination + DTO)
    // URL: GET http://localhost:8081/roles?name=admin&page=0&size=5
    @GetMapping
    public ResponseEntity<Page<RoleDTO>> getAllRoles(RoleFilter filter, Pageable pageable) {
        return ResponseEntity.ok(roleService.getAllRoles(filter, pageable)
                .map(roleMapper::toDTO));
    }

    // 2. GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(roleMapper.toDTO(roleService.getRoleById(id)));
    }

    // 3. GET BY NAME
    @GetMapping("/by-name")
    public ResponseEntity<RoleDTO> getByName(@RequestParam String name) {
        return ResponseEntity.ok(roleMapper.toDTO(roleService.getByName(name)));
    }

    // 4. CREATE (DTO + Validation)
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        var role = roleMapper.toEntity(roleDTO);
        var savedRole = roleService.addRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleMapper.toDTO(savedRole));
    }

    // 5. PARTIAL UPDATE (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<RoleDTO> partialUpdate(@PathVariable Integer id, @RequestBody RoleDTO roleDTO) {
        var rolePayload = roleMapper.toEntity(roleDTO);
        var updatedRole = roleService.update(id, rolePayload);
        return ResponseEntity.ok(roleMapper.toDTO(updatedRole));
    }

    // 6. DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}