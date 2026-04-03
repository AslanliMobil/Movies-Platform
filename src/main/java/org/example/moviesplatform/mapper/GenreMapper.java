package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.GenreDTO;
import org.example.moviesplatform.entity.Genre;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreDTO toDTO(Genre genre);

    @Mapping(target = "id", ignore = true)
    Genre toEntity(GenreDTO dto);

    List<GenreDTO> toDTOList(List<Genre> genres);

    /**
     * Mövcud janrı yeniləmək üçün.
     * Əgər DTO-da ad null-dursa, köhnə adı silmir.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateGenreFromDto(GenreDTO dto, @MappingTarget Genre genre);
}