package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.UserDTO;
import org.example.moviesplatform.entity.User;
import org.example.moviesplatform.error.model.UserNotFoundException;
import org.example.moviesplatform.mapper.UserMapper;
import org.example.moviesplatform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // GET ALL
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    // GET BY USERNAME
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // GET BY SURNAME
    public User getBySurname(String surname) {
        return userRepository.findByLastName(surname).stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // CREATE USER
    @Transactional
    public UserDTO createUser(UserDTO dto) {
        User user = userMapper.toUserAdd(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        // Tarixləri set etməyi unutmayın
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }


    @Transactional
    public UserDTO updateUserPartial(Integer id, UserDTO dto) { // Geri dönüş tipi UserDTO olsun
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userMapper.updateUser(user, dto);

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return userMapper.toUserDTO(savedUser); // Entity-ni DTO-ya çevirib qaytarırıq
    }

    // DELETE USER
    @Transactional
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
