package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.UserDTO;
import org.example.moviesplatform.entity.User;
import java.util.List;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);

    @Mapping(target = "passwordHash", ignore = true) // Service hash edir
    User toUserAdd(UserDTO dto);

    List<UserDTO> toDtoList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserDTO userDTO);
}
