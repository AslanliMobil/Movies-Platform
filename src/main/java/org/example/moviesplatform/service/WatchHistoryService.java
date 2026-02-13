package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.WatchHistoryDTO;
import org.example.moviesplatform.entity.Movie;
import org.example.moviesplatform.entity.WatchHistory;
import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.example.moviesplatform.error.model.MovieNotFoundException;
import org.example.moviesplatform.error.model.WatchHistoryNotFoundException;
import org.example.moviesplatform.mapper.WatchHistoryMapper;
import org.example.moviesplatform.repository.MovieRepository;
import org.example.moviesplatform.security.repository.UserRepository;
import org.example.moviesplatform.repository.WatchHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatchHistoryService {

    private final WatchHistoryRepository watchHistoryRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final WatchHistoryMapper watchHistoryMapper;

    @Transactional(readOnly = true)
    public Page<WatchHistoryDTO> getHistoryByUserId(Integer userId, Pageable pageable) {
        log.debug("User {} üçün bütün tarixçə səhifələnmiş formada çəkilir", userId);
        // DÜZƏLİŞ: findByUserId -> findByUserEntity_Id
        return watchHistoryRepository.findByUserEntity_Id(userId, pageable)
                .map(watchHistoryMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<WatchHistoryDTO> getIncompleteHistory(Integer userId) {
        log.debug("User {} üçün yarımçıq qalmış filmlər siyahısı çəkilir", userId);
        // DÜZƏLİŞ: findByUserIdAnd... -> findByUserEntity_IdAnd...
        return watchHistoryMapper.toDTOList(
                watchHistoryRepository.findByUserEntity_IdAndIsCompletedFalseOrderByLastWatchedAtDesc(userId)
        );
    }

    @Transactional
    public WatchHistoryDTO saveOrUpdateHistory(WatchHistoryDTO dto) {
        log.info("Proqres yenilənir: User {}, Movie {}, Saniyə {}",
                dto.getUserId(), dto.getMovieId(), dto.getWatchedSeconds());

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException("Film tapılmadı: " + dto.getMovieId()));

        WatchHistory history = watchHistoryRepository
                // DÜZƏLİŞ: findByUserIdAndMovieId -> findByUserEntity_IdAndMovieId
                .findByUserEntity_IdAndMovieId(dto.getUserId(), dto.getMovieId())
                .orElseGet(() -> {
                    WatchHistory newHistory = watchHistoryMapper.toEntity(dto);
                    newHistory.setUserEntity(userRepository.getReferenceById(dto.getUserId().longValue()));
                    newHistory.setMovie(movie);
                    newHistory.setWatchCount(0);
                    return newHistory;
                });

        watchHistoryMapper.updateEntityFromDto(dto, history);
        history.setWatchCount(history.getWatchCount() + 1);
        history.calculateProgress(movie.getDuration());

        WatchHistory saved = watchHistoryRepository.save(history);
        return watchHistoryMapper.toDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!watchHistoryRepository.existsById(id)) {
            throw new WatchHistoryNotFoundException("Tarixçə qeydi tapılmadı ID: " + id);
        }
        watchHistoryRepository.deleteById(id);
        log.warn("Tarixçə qeydi silindi ID: {}", id);
    }

    @Transactional
    public void clearHistoryByUserId(Integer userId) {
        log.warn("User {} üçün bütün tarixçə təmizlənir!", userId);
        // DÜZƏLİŞ: deleteAllByUserId metodunun Repository-dəki JPQL qarşılığı artıq userEntity-yə baxır
        watchHistoryRepository.deleteAllByUserId(userId);
    }
}