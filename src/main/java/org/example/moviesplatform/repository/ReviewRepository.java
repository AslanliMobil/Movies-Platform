package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>, JpaSpecificationExecutor<Review> {

    /**
     * Müəyyən bir filmə aid bütün rəyləri səhifələnmiş formada gətirir.
     */
    Page<Review> findByMovieId(Integer movieId, Pageable pageable);

    /**
     * Bir istifadəçinin bütün rəylərini səhifələnmiş formada gətirir.
     */
    Page<Review> findByUserId(Integer userId, Pageable pageable);

    /**
     * Təkrar rəy yoxlaması: Bir user bir filmə yalnız bir dəfə rəy yaza bilsin.
     */
    boolean existsByUserIdAndMovieId(Integer userId, Integer movieId);

    /**
     * Filmin orta reytinqini (AVG) hesablayan JPQL sorğusu.
     * Bu sorğu bütün rəylərin ulduzlarını toplayıb sayına bölür.
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = :movieId")
    Double calculateAverageRating(@Param("movieId") Integer movieId);

    /**
     * ID-yə görə rəyi taparkən User və Movie datalarını birbaşa gətirmək üçün (N+1 probleminin qarşısını alır).
     */
    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.movie WHERE r.id = :id")
    Optional<Review> findByIdWithDetails(@Param("id") Integer id);
}