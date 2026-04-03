package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.UserDTO;
import org.example.moviesplatform.dto.UserUpdateDTO;
import org.example.moviesplatform.entity.Role;
import org.example.moviesplatform.error.model.ResourceAlreadyExistsException;
import org.example.moviesplatform.error.model.UserNotFoundException;
import org.example.moviesplatform.mapper.UserMapper;
import org.example.moviesplatform.model.UserFilter;
import org.example.moviesplatform.repository.RoleRepository;
import org.example.moviesplatform.security.repository.UserRepository;
import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.example.moviesplatform.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("adminUserService")
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(UserFilter filter, Pageable pageable) {
        log.debug("İstifadəçilər filtrlənir: {}", filter);
        Specification<UserEntity> spec = UserSpecification.getSpecification(filter);
        return userRepository.findAll(spec, pageable).map(userMapper::toUserDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Integer id) {
        log.info("İstifadəçi ID-yə görə axtarılır: {}", id);
        UserEntity user = findEntityById(id);
        return userMapper.toUserDTO(user);
    }

    @Transactional(readOnly = true)
    public UserEntity findEntityById(Integer id) {
        return userRepository.findById(id.longValue())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserDTO createUser(UserDTO dto) {
        log.info("Yeni istifadəçi yaradılır: {}", dto.getUsername());

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResourceAlreadyExistsException("Username artıq istifadə olunub: " + dto.getUsername());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email artıq istifadə olunub: " + dto.getEmail());
        }

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role (ROLE_USER) not found in database!"));

        UserEntity user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.getRoles().add(defaultRole);

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
        user.setDeleted(true);
        userRepository.save(user);
        log.warn("İstifadəçi soft-delete edildi (is_deleted=true): ID {}", id);
    }

    @Transactional
    public UserDTO restoreUser(Integer id) {
        log.info("Silinmiş istifadəçi bərpa edilir: ID {}", id);
        UserEntity user = findEntityById(id);

        if (!user.isDeleted()) {
            throw new RuntimeException("Bu istifadəçi onsuz da aktivdir və silinməyib!");
        }

        user.setDeleted(false);
        UserEntity restoredUser = userRepository.save(user);

        log.info("İstifadəçi uğurla bərpa edildi: ID {}, Username: {}", restoredUser.getId(), restoredUser.getUsername());
        return userMapper.toUserDTO(restoredUser);
    }
}