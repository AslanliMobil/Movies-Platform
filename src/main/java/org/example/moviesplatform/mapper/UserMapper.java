package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.UserDTO;
import org.example.moviesplatform.dto.UserUpdateDTO;
import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateEntityFromUpdateDto(@MappingTarget UserEntity user, UserUpdateDTO dto);

    @Mapping(target = "password", ignore = true)
    UserDTO toUserDTO(UserEntity user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password")
    UserEntity toEntity(UserDTO dto);

    List<UserDTO> toDtoList(List<UserEntity> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntity(@MappingTarget UserEntity user, UserDTO dto);
}