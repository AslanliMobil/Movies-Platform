package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.GenreDTO;
import org.example.moviesplatform.entity.Genre;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDTO toDTO(Genre genre);
    Genre toEntity(GenreDTO dto);
    List<GenreDTO> toDTOList(List<Genre> genres);
}

