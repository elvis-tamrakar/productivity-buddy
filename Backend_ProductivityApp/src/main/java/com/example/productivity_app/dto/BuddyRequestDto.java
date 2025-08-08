package com.example.productivity_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuddyRequestDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String status;
}
