package com.example.productivity_app.mapper;

import com.example.productivity_app.dto.BuddyRequestDto;
import com.example.productivity_app.entity.BuddyRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BuddyRequestMapper {
    BuddyRequestDto toDto(BuddyRequest request);
    BuddyRequest toEntity(BuddyRequestDto dto);
}