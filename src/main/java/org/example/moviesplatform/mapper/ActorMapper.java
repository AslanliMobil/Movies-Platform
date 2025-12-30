package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.ActorDTO;
import org.example.moviesplatform.entity.Actor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActorMapper {
    ActorDTO toDTO(Actor actor);
    Actor toEntity(ActorDTO dto);
    List<ActorDTO> toDTOList(List<Actor> actors);
}

