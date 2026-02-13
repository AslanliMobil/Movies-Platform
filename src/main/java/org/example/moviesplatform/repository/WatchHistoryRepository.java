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

    // 1. DÜZƏLİŞ: findByUserId yerinə findByUserEntity_Id (Entity-dəki obyekt adı + _ + sahə adı)
    @EntityGraph(attributePaths = {"movie"})
    Optional<WatchHistory> findByUserEntity_IdAndMovieId(Integer userId, Integer movieId);

    // 2. DÜZƏLİŞ: findByUserId -> findByUserEntity_Id
    @EntityGraph(attributePaths = {"movie"})
    Page<WatchHistory> findByUserEntity_Id(Integer userId, Pageable pageable);

    // 3. DÜZƏLİŞ: findByUserIdAndIsCompletedFalse -> findByUserEntity_IdAndIsCompletedFalse
    @EntityGraph(attributePaths = {"movie"})
    List<WatchHistory> findByUserEntity_IdAndIsCompletedFalseOrderByLastWatchedAtDesc(Integer userId);

    // 4. DÜZƏLİŞ: findTop5ByUserId -> findTop5ByUserEntity_Id
    List<WatchHistory> findTop5ByUserEntity_IdOrderByWatchCountDesc(Integer userId);

    // 5. DÜZƏLİŞ: w.user.id -> w.userEntity.id (JPQL-də entity sahə adına uyğun olmalıdır)
    @Modifying
    @Query("DELETE FROM WatchHistory w WHERE w.userEntity.id = :userId")
    void deleteAllByUserId(@Param("userId") Integer userId);

    // 6. Filmin neçə nəfər tərəfindən bitirildiyini saymaq (Burada movieId dəyişməyibsə qalır)
    long countByMovieIdAndIsCompletedTrue(Integer movieId);
}