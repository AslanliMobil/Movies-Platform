package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.RoleDTO;
import org.example.moviesplatform.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy; // Bunu əlavə etdik
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE // Tapılmayan sahələri avtomatik keçir
)
public interface RoleMapper {

    // Entity -> DTO
    RoleDTO toDTO(Role role);

    // DTO -> Entity
    @Mapping(target = "id", ignore = true)
    // "users", "createdAt" və "updatedAt" artıq unmappedTargetPolicy tərəfindən idarə olunacaq
    Role toEntity(RoleDTO roleDTO);

    List<RoleDTO> toDTOList(List<Role> roles);
}