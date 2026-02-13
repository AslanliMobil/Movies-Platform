package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer>, JpaSpecificationExecutor<Movie> {

    /**
     * Aktiv (silinməmiş) filmlər arasında adına görə axtarış.
     */
    Optional<Movie> findByTitleIgnoreCaseAndIsDeletedFalse(String title);

    /**
     * Yeni film yaradılarkən eyni adda aktiv filmin olub-olmadığını yoxlayır.
     */
    boolean existsByTitleIgnoreCaseAndIsDeletedFalse(String title);

    /**
     * BƏRPA ÜÇÜN: Silinmiş bir filmi ID-sinə görə tapmaq.
     * Hibernate-in @Where filtrindən yan keçmək üçün nativeQuery istifadə olunur.
     */
    @Query(value = "SELECT * FROM movies WHERE id = :id AND is_deleted = true", nativeQuery = true)
    Optional<Movie> findDeletedMovieById(@Param("id") Integer id);

    /**
     * Müəyyən bir rejissorun ən yüksək reytinqli aktiv filmini tapmaq.
     */
    @Query("SELECT m FROM Movie m WHERE m.director.id = :directorId AND m.isDeleted = false ORDER BY m.averageRating DESC LIMIT 1")
    Optional<Movie> findTopByDirectorOrderByAverageRatingDesc(@Param("directorId") Integer directorId);

    /**
     * Zibil Qutusu: Bütün silinmiş filmlərin siyahısı.
     */
    @Query(value = "SELECT * FROM movies WHERE is_deleted = true", nativeQuery = true)
    Page<Movie> findAllDeletedMovies(Pageable pageable);
}