package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>, JpaSpecificationExecutor<Review> {
    List<Review> findByMovieId(Integer movieId);
    List<Review> findByUserId(Integer userId);
}
