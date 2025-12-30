package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.DirectorDTO;
import org.example.moviesplatform.entity.Director;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DirectorMapper {
    DirectorDTO toDTO(Director director);
    Director toEntity(DirectorDTO dto);
    List<DirectorDTO> toDTOList(List<Director> directors);
}
