package org.example.moviesplatform.security.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.entity.Role;
import org.example.moviesplatform.repository.RoleRepository;
import org.example.moviesplatform.security.model.RegisterRequest;
import org.example.moviesplatform.security.repository.UserRepository;
import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service("authUserService")
@RequiredArgsConstructor
@Slf4j
public class AuthUserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.security.jwt.secret-key}")
    private String secretKeyString;

    @Value("${application.security.jwt.expiration-ms}")
    private long jwtExpiration;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Role adlarını (məs: ROLE_USER) SimpleGrantedAuthority-yə çeviririk
        Set<GrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // Əgər bazada ROLE_ prefixi yoxdursa: "ROLE_" + role.getName()
                .collect(Collectors.toSet());

        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Qeydiyyat xətası: Username artıq mövcuddur: {}", request.getUsername());
            throw new RuntimeException("Username already exists!");
        }

        // Qeydiyyat zamanı standart ROLE_USER təyin edirik
        // Diqqət: Bazada 'ROLE_USER' adlı sətir mütləq olmalıdır!
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role (ROLE_USER) not found in database!"));

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(defaultRole))
                .enabled(true) // İstifadəçi aktiv olsun
                .isDeleted(false)
                .build();

        userRepository.save(user);
        log.info("Yeni istifadəçi qeydiyyatdan keçdi: {}", user.getUsername());
    }

    public String generateToken(Authentication authResult) {
        SecretKey key = Keys.hmacShaKeyFor(secretKeyString.getBytes());

        // Authorities siyahısını ["ROLE_USER", "ROLE_ADMIN"] formatında claim-ə əlavə edirik
        return Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }
}