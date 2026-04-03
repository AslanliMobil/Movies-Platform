package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.DirectorDTO;
import org.example.moviesplatform.entity.Director;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DirectorMapper {

    DirectorDTO toDTO(Director director);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movies", ignore = true)
    Director toEntity(DirectorDTO dto);

    List<DirectorDTO> toDTOList(List<Director> directors);

    /**
     * Mövcud rejissoru yeniləmək üçün (PUT/PATCH).
     * Rejissorun id-si və filmləri bu mapper vasitəsilə dəyişdirilməməlidir.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movies", ignore = true)
    void updateDirectorFromDto(DirectorDTO dto, @MappingTarget Director director);
}