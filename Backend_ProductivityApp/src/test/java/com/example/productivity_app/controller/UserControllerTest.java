package com.example.productivity_app.controller;

import com.example.productivity_app.dto.LoginRequestDto;
import com.example.productivity_app.dto.LoginResponseDto;
import com.example.productivity_app.dto.RegisterDto;
import com.example.productivity_app.dto.UsersDto;
import com.example.productivity_app.entity.Users;
import com.example.productivity_app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Unit Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Users testUser;
    private UsersDto testUserDto;
    private RegisterDto testRegisterDto;
    private LoginRequestDto testLoginRequest;
    private LoginResponseDto testLoginResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        testUser = new Users();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setUsername("testuser");

        testUserDto = new UsersDto();
        testUserDto.setId(1L);
        testUserDto.setEmail("test@example.com");
        testUserDto.setUsername("testuser");

        testRegisterDto = new RegisterDto();
        testRegisterDto.setEmail("newuser@example.com");
        testRegisterDto.setUsername("newuser");
        testRegisterDto.setPassword("password123");

        testLoginRequest = new LoginRequestDto();
        testLoginRequest.setEmail("test@example.com");
        testLoginRequest.setPassword("password123");

        testLoginResponse = new LoginResponseDto("jwtToken", testUserDto);
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() throws Exception {
        // Arrange
        when(userService.createUser(any(RegisterDto.class))).thenReturn(testUserDto);

        // Act & Assert
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRegisterDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService).createUser(any(RegisterDto.class));
    }

    @Test
    @DisplayName("Should return 400 for invalid register data")
    void shouldReturn400ForInvalidRegisterData() throws Exception {
        // Arrange
        RegisterDto invalidDto = new RegisterDto();
        invalidDto.setEmail("invalid-email");
        invalidDto.setUsername(""); // Empty username
        invalidDto.setPassword("123"); // Too short password

        // Act & Assert
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginUserSuccessfully() throws Exception {
        // Arrange
        when(userService.login(anyString(), anyString())).thenReturn(testLoginResponse);

        // Act & Assert
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwtToken"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));

        verify(userService).login("test@example.com", "password123");
    }

    @Test
    @DisplayName("Should return 401 for invalid login credentials")
    void shouldReturn401ForInvalidLoginCredentials() throws Exception {
        // Arrange
        when(userService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLoginRequest)))
                .andExpect(status().isUnauthorized());

        verify(userService).login("test@example.com", "password123");
    }

    @Test
    @DisplayName("Should return 400 for invalid login data")
    void shouldReturn400ForInvalidLoginData() throws Exception {
        // Arrange
        LoginRequestDto invalidDto = new LoginRequestDto();
        invalidDto.setEmail("invalid-email");
        invalidDto.setPassword(""); // Empty password

        // Act & Assert
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).login(anyString(), anyString());
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() throws Exception {
        // Arrange
        when(userService.getUsersById(1L)).thenReturn(testUserDto);

        // Act & Assert
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService).getUsersById(1L);
    }

    @Test
    @DisplayName("Should return 404 for non-existent user")
    void shouldReturn404ForNonExistentUser() throws Exception {
        // Arrange
        when(userService.getUsersById(999L))
                .thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());

        verify(userService).getUsersById(999L);
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() throws Exception {
        // Arrange
        UsersDto updateDto = new UsersDto();
        updateDto.setUsername("updateduser");
        updateDto.setEmail("updated@example.com");

        when(userService.updateUser(1L, updateDto)).thenReturn(updateDto);

        // Act & Assert
        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        verify(userService).updateUser(1L, updateDto);
    }

    @Test
    @DisplayName("Should return 400 for invalid update data")
    void shouldReturn400ForInvalidUpdateData() throws Exception {
        // Arrange
        UsersDto invalidDto = new UsersDto();
        invalidDto.setEmail("invalid-email");
        invalidDto.setUsername(""); // Empty username

        // Act & Assert
        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(anyLong(), any());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUser(1L);

        // Act & Assert
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("Should handle validation errors properly")
    void shouldHandleValidationErrorsProperly() throws Exception {
        // Arrange
        RegisterDto invalidDto = new RegisterDto();
        // Missing required fields

        // Act & Assert
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}
