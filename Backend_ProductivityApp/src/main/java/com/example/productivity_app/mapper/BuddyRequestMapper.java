package com.example.productivity_app.mapper;

import com.example.productivity_app.dto.BuddyRequestDto;
import com.example.productivity_app.entity.BuddyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BuddyRequestMapper {
    
    @Mapping(source = "requester.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
    BuddyRequestDto toDto(BuddyRequest request);
    
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    BuddyRequest toEntity(BuddyRequestDto dto);
}