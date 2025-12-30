package org.example.moviesplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.entity.Role;
import org.example.moviesplatform.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // 1. GET ALL (Pagination daxil olmaqla)
    // URL: GET http://localhost:8081/roles
    // URL Pagination üçün: GET http://localhost:8081/roles?page=0&size=5
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // 2. GET BY ID
    // URL: GET http://localhost:8081/roles/1
    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    // 3. GET BY NAME
    // URL: GET http://localhost:8081/roles/search?name=ROLE_USER
    @GetMapping("/search")
    public ResponseEntity<Role> getByName(@RequestParam String name) {
        return ResponseEntity.ok(roleService.getByName(name));
    }

    // 4. CREATE
    // URL: POST http://localhost:8081/roles
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.addRole(role));
    }

    // 5. PARTIAL UPDATE (PATCH)
    // URL: PATCH http://localhost:8081/roles/1
    @PatchMapping("/{id}")
    public ResponseEntity<Role> partialUpdate(@PathVariable Integer id, @RequestBody Role role) {
        return ResponseEntity.ok(roleService.update(id, role));
    }

    // 6. DELETE
    // URL: DELETE http://localhost:8081/roles/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}