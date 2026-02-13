package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer>, JpaSpecificationExecutor<Actor> {
    boolean existsByNameIgnoreCase(String name);
    List<Actor> findByNameContainingIgnoreCase(String name);
}
