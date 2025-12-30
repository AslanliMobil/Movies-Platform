package org.example.moviesplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.UserDTO;
import org.example.moviesplatform.entity.User;
import org.example.moviesplatform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final org.example.moviesplatform.mapper.UserMapper userMapper; // Bu əlavə olundu

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return ResponseEntity.ok(userMapper.toUserDTO(user));
    }

    @GetMapping("/surname/{surname}")
    public ResponseEntity<UserDTO> getBySurname(@PathVariable String surname) {
        User user = userService.getBySurname(surname);
        return ResponseEntity.ok(userMapper.toUserDTO(user)); // Artıq DTO qaytarır
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
        UserDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.updateUserPartial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
