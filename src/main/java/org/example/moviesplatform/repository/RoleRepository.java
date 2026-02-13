package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // 1. Bu importu əlavə et
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// 2. JpaSpecificationExecutor<Role> əlavə et
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    // Qeyd: Artıq findAllByFilter metoduna ehtiyacın yoxdur,
    // çünki RoleSpecification eyni işi daha peşəkar (və bytea xətasız) görür.
    @Query("SELECT r FROM Role r WHERE " +
            "(:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:description IS NULL OR LOWER(r.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    Page<Role> findAllByFilter(@Param("name") String name,
                               @Param("description") String description,
                               Pageable pageable);
}