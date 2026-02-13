package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer>, JpaSpecificationExecutor<Director> {
    boolean existsByNameIgnoreCase(String name);
}
