package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.MovieDTO;
import org.example.moviesplatform.entity.Movie;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {GenreMapper.class, ActorMapper.class, DirectorMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MovieMapper {

    MovieDTO toDTO(Movie movie);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    Movie toEntity(MovieDTO dto);

    List<MovieDTO> toDTOList(List<Movie> movies);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    void updateMovieFromDto(MovieDTO dto, @MappingTarget Movie movie);
}