package com.example.productivity_app.mapper;

import com.example.productivity_app.dto.UsersDto;
import com.example.productivity_app.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersDto toDto(Users users);

    Users toEntity(UsersDto usersDto);
}