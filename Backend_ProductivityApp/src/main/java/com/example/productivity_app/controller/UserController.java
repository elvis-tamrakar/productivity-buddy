package com.example.productivity_app.controller;

import com.example.productivity_app.dto.LoginRequestDto;
import com.example.productivity_app.dto.RegisterDto;
import com.example.productivity_app.dto.UsersDto;
import com.example.productivity_app.entity.Users;
import com.example.productivity_app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsersDto> createUser(@RequestBody RegisterDto registerDto) {
        UsersDto created = userService.createUser(registerDto);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<UsersDto> loginUser(@RequestBody LoginRequestDto loginRequest) {
        try {
            UsersDto user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDto> getUserById(@PathVariable long id) {
        try {
            UsersDto dto = userService.getUsersById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersDto> updateUser(@PathVariable long id, @RequestBody UsersDto dto) {
        UsersDto updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
