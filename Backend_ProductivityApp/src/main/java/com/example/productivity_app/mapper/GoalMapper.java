package com.example.productivity_app.mapper;

import com.example.productivity_app.dto.GoalDto;
import com.example.productivity_app.entity.Goal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CheckpointMapper.class)
public interface GoalMapper {
    GoalDto toDto(Goal goal);
    Goal toEntity(GoalDto dto);
}
