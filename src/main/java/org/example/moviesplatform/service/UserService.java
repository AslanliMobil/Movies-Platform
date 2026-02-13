package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.UserDTO;
import org.example.moviesplatform.dto.UserUpdateDTO;
// 1. DÜZƏLİŞ: Köhnə User yerinə UserEntity
import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.example.moviesplatform.error.model.UserNotFoundException;
import org.example.moviesplatform.mapper.UserMapper;
import org.example.moviesplatform.model.UserFilter;
// 2. DÜZƏLİŞ: UserRepository artıq security paketindədir
import org.example.moviesplatform.security.repository.UserRepository;
import org.example.moviesplatform.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("adminUserService") // Adını dəyişdik ki, AuthUserService ilə toqquşmasın
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(UserFilter filter, Pageable pageable) {
        log.debug("İstifadəçilər filtrlənir: {}", filter);
        // UserSpecification daxilində də User -> UserEntity dəyişməli ola bilər
        Specification<UserEntity> spec = UserSpecification.getSpecification(filter);
        return userRepository.findAll(spec, pageable).map(userMapper::toUserDTO);
    }

    @Transactional(readOnly = true)
    public UserEntity findEntityById(Integer id) {
        // 3. DÜZƏLİŞ: Integer ID-ni Long-a çeviririk
        return userRepository.findById(id.longValue())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserDTO createUser(UserDTO dto) {
        log.info("Yeni istifadəçi yaradılır: {}", dto.getUsername());

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username artıq istifadə olunub: " + dto.getUsername());
        }

        UserEntity user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO updateUserPartial(Integer id, UserUpdateDTO dto) {
        log.info("İstifadəçi yenilənir: ID {}", id);
        UserEntity user = findEntityById(id);

        userMapper.updateEntityFromUpdateDto(user, dto);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        UserEntity savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }

    @Transactional
    public void deleteUser(Integer id) {
        UserEntity user = findEntityById(id);
        userRepository.delete(user);
        log.warn("İstifadəçi silindi: ID {}", id);
    }
}