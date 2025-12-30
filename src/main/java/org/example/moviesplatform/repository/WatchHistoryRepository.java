package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Integer>, JpaSpecificationExecutor<WatchHistory> {
    List<WatchHistory> findByUserIdOrderByLastWatchedAtDesc(Integer userId);
    Optional<WatchHistory> findByUserIdAndMovieId(Integer userId, Integer movieId);
}
