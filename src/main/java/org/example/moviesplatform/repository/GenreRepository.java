package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer>, JpaSpecificationExecutor<Genre> {

    /**
     * Janrın adına görə tam axtarış.
     */
    Optional<Genre> findByName(String name);

    /**
     * Ad üzrə bazada belə bir qeydin olub-olmadığını yoxlayır.
     * IgnoreCase - böyük/kiçik hərf fərqi qoymur (məs: 'Action' və 'action' eyni sayılır).
     */
    boolean existsByNameIgnoreCase(String name);
}