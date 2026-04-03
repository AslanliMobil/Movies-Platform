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

    List<Wishlist> findByUserEntity_IdOrderByCreatedAtDesc(Integer userId);

    boolean existsByIdUserIdAndIdMovieId(Integer userId, Integer movieId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist w WHERE w.id.userId = :userId AND w.id.movieId = :movieId")
    void deleteByUserIdAndMovieId(@Param("userId") Integer userId, @Param("movieId") Integer movieId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist w WHERE w.id.userId = :userId")
    void deleteAllByUserId(@Param("userId") Integer userId);

    Optional<Wishlist> findByIdUserIdAndIdMovieId(Integer userId, Integer movieId);
}