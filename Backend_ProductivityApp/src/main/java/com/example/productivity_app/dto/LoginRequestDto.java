package com.example.productivity_app.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
