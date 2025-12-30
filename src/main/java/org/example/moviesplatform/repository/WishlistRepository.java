package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.Wishlist;
import org.example.moviesplatform.entity.WishlistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, WishlistId>, JpaSpecificationExecutor<Wishlist> {
    List<Wishlist> findByUserId(Integer userId);
    void deleteByUserIdAndMovieId(Integer userId, Integer movieId);
}