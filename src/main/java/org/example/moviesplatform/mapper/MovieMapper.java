package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.MovieDTO;
import org.example.moviesplatform.entity.Movie;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {GenreMapper.class, ActorMapper.class, DirectorMapper.class})
public interface MovieMapper {

    MovieDTO toDTO(Movie movie);

    Movie toEntity(MovieDTO dto);

    List<MovieDTO> toDTOList(List<Movie> movies);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMovie(@MappingTarget Movie movie, MovieDTO dto);
}
