package com.example.productivity_app.mapper;

import com.example.productivity_app.dto.CheckpointDto;
import com.example.productivity_app.entity.Checkpoint;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CheckpointMapper {
    CheckpointDto toDto(Checkpoint checkpoint);
    Checkpoint toEntity(CheckpointDto dto);
}