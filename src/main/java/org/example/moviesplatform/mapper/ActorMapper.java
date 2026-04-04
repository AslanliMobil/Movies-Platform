package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.ActorDTO;
import org.example.moviesplatform.entity.Actor;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActorMapper {


    ActorDTO toDTO(Actor actor);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Actor toEntity(ActorDTO dto);

    List<ActorDTO> toDTOList(List<Actor> actors);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateActorFromDto(ActorDTO dto, @MappingTarget Actor actor);
}