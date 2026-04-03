package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT r FROM Role r WHERE " +
            "(:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:description IS NULL OR LOWER(r.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    Page<Role> findAllByFilter(@Param("name") String name,
                               @Param("description") String description,
                               Pageable pageable);
}