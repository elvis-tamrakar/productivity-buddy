package com.example.productivity_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckpointDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;
    private LocalDate completedDate;
}
