package com.example.productivity_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalDto {
    private Long id;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private List<CheckpointDto> checkpoints;
}
