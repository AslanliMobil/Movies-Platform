package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.WatchHistoryDTO;
import org.example.moviesplatform.entity.WatchHistory;
import org.example.moviesplatform.mapper.WatchHistoryMapper;
import org.example.moviesplatform.repository.WatchHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatchHistoryService {

    private final WatchHistoryRepository watchHistoryRepository;
    private final WatchHistoryMapper watchHistoryMapper;

    public List<WatchHistoryDTO> getHistoryByUserId(Integer userId) {
        return watchHistoryMapper.toDTOList(
                watchHistoryRepository.findByUserIdOrderByLastWatchedAtDesc(userId)
        );
    }

    @Transactional
    public WatchHistoryDTO saveOrUpdateHistory(WatchHistoryDTO dto) {
        // Əvvəl baxıb-baxmadığını yoxlayırıq
        WatchHistory history = watchHistoryRepository
                .findByUserIdAndMovieId(dto.getUserId(), dto.getMovieId())
                .orElse(watchHistoryMapper.toEntity(dto));

        history.setStoppedAt(dto.getStoppedAt());
        history.setIsFinished(dto.getIsFinished());
        history.setLastWatchedAt(LocalDateTime.now());

        WatchHistory saved = watchHistoryRepository.save(history);
        return watchHistoryMapper.toDTO(saved);
    }

    // WatchHistoryService.java daxilində
    // WatchHistoryService.java daxilində

    @Transactional
    public void delete(Integer id) {
        log.debug("Deleting watch history entry with id: {}", id);

        // Əgər ID bazada yoxdursa xəta atırıq (Opsional, amma məsləhətdir)
        if (!watchHistoryRepository.existsById(id)) {
            throw new RuntimeException("Watch history entry not found with id: " + id);
        }

        watchHistoryRepository.deleteById(id);
        log.info("Successfully deleted watch history entry with id: {}", id);
    }
}
