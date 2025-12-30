package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.WatchHistoryDTO;
import org.example.moviesplatform.entity.WatchHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WatchHistoryMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "movie.title", target = "movieTitle")
    WatchHistoryDTO toDTO(WatchHistory history);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "movieId", target = "movie.id")
    WatchHistory toEntity(WatchHistoryDTO dto);

    List<WatchHistoryDTO> toDTOList(List<WatchHistory> historyList);
}
