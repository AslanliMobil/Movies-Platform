package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.ActorDTO;
import org.example.moviesplatform.entity.Actor;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActorMapper {

    // Entity -> DTO
    ActorDTO toDTO(Actor actor);

    // DTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    // 'movies' sahəsi Entity-də olmadığı üçün buradan çıxarıldı
    Actor toEntity(ActorDTO dto);

    // Siyahı çevrilməsi
    List<ActorDTO> toDTOList(List<Actor> actors);

    /**
     * Mövcud Actor obyektini yeniləmək üçün (Partial Update).
     * nullValuePropertyMappingStrategy = IGNORE: DTO-da null olan sahələr
     * bazadakı mövcud məlumatı əzmir.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateActorFromDto(ActorDTO dto, @MappingTarget Actor actor);
}