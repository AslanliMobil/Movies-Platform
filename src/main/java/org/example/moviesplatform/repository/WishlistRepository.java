package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.Wishlist;
import org.example.moviesplatform.entity.WishlistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, WishlistId>, JpaSpecificationExecutor<Wishlist> {

    /**
     * DÜZƏLİŞ: findByUserId -> findByUserEntity_Id
     * Wishlist daxilindəki userEntity-nin id sahəsinə görə axtarır.
     */
    List<Wishlist> findByUserEntity_IdOrderByCreatedAtDesc(Integer userId);

    /**
     * Composite ID daxilindəki sahələrə görə yoxlama.
     */
    boolean existsByIdUserIdAndIdMovieId(Integer userId, Integer movieId);

    /**
     * JPQL Sorğusu: w.id.userId artıq WishlistId klassındakı sahəyə baxır (düzgündür).
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist w WHERE w.id.userId = :userId AND w.id.movieId = :movieId")
    void deleteByUserIdAndMovieId(@Param("userId") Integer userId, @Param("movieId") Integer movieId);

    /**
     * İstifadəçinin bütün siyahısını təmizləmək.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist w WHERE w.id.userId = :userId")
    void deleteAllByUserId(@Param("userId") Integer userId);

    /**
     * Composite ID daxilində axtarış.
     */
    Optional<Wishlist> findByIdUserIdAndIdMovieId(Integer userId, Integer movieId);
}