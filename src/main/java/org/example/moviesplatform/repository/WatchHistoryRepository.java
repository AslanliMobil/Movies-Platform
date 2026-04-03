package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.WatchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long>, JpaSpecificationExecutor<WatchHistory> {

    @EntityGraph(attributePaths = {"movie"})
    Optional<WatchHistory> findByUserEntity_IdAndMovieId(Integer userId, Integer movieId);

    @EntityGraph(attributePaths = {"movie"})
    Page<WatchHistory> findByUserEntity_Id(Integer userId, Pageable pageable);

    @EntityGraph(attributePaths = {"movie"})
    List<WatchHistory> findByUserEntity_IdAndIsCompletedFalseOrderByLastWatchedAtDesc(Integer userId);

    List<WatchHistory> findTop5ByUserEntity_IdOrderByWatchCountDesc(Integer userId);

    @Modifying
    @Query("DELETE FROM WatchHistory w WHERE w.userEntity.id = :userId")
    void deleteAllByUserId(@Param("userId") Integer userId);

    long countByMovieIdAndIsCompletedTrue(Integer movieId);
}