package com.pamelamoreiras.bookstoremanager.users.mapper;

import com.pamelamoreiras.bookstoremanager.users.dto.UserDTO;
import com.pamelamoreiras.bookstoremanager.users.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toModel(UserDTO userDTO);

    UserDTO toDTO(User user);
}
