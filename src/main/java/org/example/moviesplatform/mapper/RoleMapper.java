package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.RoleDTO;
import org.mapstruct.Mapper;

import javax.management.relation.Role;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toDTO(Role role);
    Role toEntity(RoleDTO dto);
    List<RoleDTO> toDTOList(List<Role> roles);
}
