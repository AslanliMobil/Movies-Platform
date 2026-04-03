package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.RoleDTO;
import org.example.moviesplatform.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleMapper {

    RoleDTO toDTO(Role role);

    @Mapping(target = "id", ignore = true)
    Role toEntity(RoleDTO roleDTO);

    List<RoleDTO> toDTOList(List<Role> roles);
}