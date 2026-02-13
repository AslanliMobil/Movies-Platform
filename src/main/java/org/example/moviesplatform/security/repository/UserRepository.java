package org.example.moviesplatform.security.repository;

import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // 1. BU LAZIMDIR
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends
        JpaRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> { // 2. Specification dəstəyi üçün əlavə etdik

    /**
     * Giriş (Login) zamanı istifadəçini tapmaq üçün istifadə olunur.
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Qeydiyyat zamanı username yoxlaması.
     */
    boolean existsByUsername(String username);

    /**
     * UserService daxilində createUser metodunda istifadə olunan email yoxlaması.
     * Əgər UserEntity-də email sahəsi yoxdursa, bu metodu silə bilərsən.
     */
    boolean existsByEmail(String email);
}