package com.example.productivity_app.dto;

import lombok.Data;

@Data
public class CheckpointDto {
    private Long id;
    private String date;
    private String status;
}
