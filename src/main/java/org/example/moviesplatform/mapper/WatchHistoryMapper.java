package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.WatchHistoryDTO;
import org.example.moviesplatform.entity.WatchHistory;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WatchHistoryMapper {

    @Mapping(source = "userEntity.id", target = "userId")
    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "movie.title", target = "movieTitle")
    WatchHistoryDTO toDTO(WatchHistory history);


    @Mapping(source = "userId", target = "userEntity.id")
    @Mapping(source = "movieId", target = "movie.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastWatchedAt", ignore = true)
    WatchHistory toEntity(WatchHistoryDTO dto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userEntity", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "watchCount", ignore = true)
    void updateEntityFromDto(WatchHistoryDTO dto, @MappingTarget WatchHistory entity);

    List<WatchHistoryDTO> toDTOList(List<WatchHistory> historyList);
}