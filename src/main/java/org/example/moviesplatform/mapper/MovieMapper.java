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

    // Entity -> DTO
    MovieDTO toDTO(Movie movie);

    // DTO -> Entity (Yeni film yaradarkən)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    // Builder problemlərindən qaçmaq üçün "deleted" və ya "isDeleted" mapping-lərini tam sildik.
    // Çünki bu sahələr default olaraq 'false' (və ya null) gedəcək, bu da bizim üçün kifayətdir.
    Movie toEntity(MovieDTO dto);

    // Siyahı çevrilməsi
    List<MovieDTO> toDTOList(List<Movie> movies);

    /**
     * Mövcud Movie obyektini yeniləmək üçün (Partial Update).
     * unmappedTargetPolicy = ReportingPolicy.IGNORE sayəsində 'deleted' kimi
     * mübahisəli sahələrə görə build xətası almayacaqsınız.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    void updateMovieFromDto(MovieDTO dto, @MappingTarget Movie movie);
}