package com.example.productivity_app.service;

import com.example.productivity_app.dto.LoginRequestDto;
import com.example.productivity_app.dto.LoginResponseDto;
import com.example.productivity_app.dto.RegisterDto;
import com.example.productivity_app.dto.UsersDto;
import com.example.productivity_app.entity.Users;
import com.example.productivity_app.mapper.UsersMapper;
import com.example.productivity_app.repository.UserRepository;
import com.example.productivity_app.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsersMapper usersMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private Users testUser;
    private UsersDto testUserDto;
    private RegisterDto testRegisterDto;
    private LoginRequestDto testLoginRequest;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");

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
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Users.class))).thenReturn(testUser);
        when(usersMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        UsersDto result = userService.createUser(testRegisterDto);

        // Assert
        assertNotNull(result);
        assertEquals(testUserDto.getEmail(), result.getEmail());
        assertEquals(testUserDto.getUsername(), result.getUsername());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(Users.class));
        verify(usersMapper).toDto(testUser);
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(usersMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        UsersDto result = userService.getUsersById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testUserDto.getId(), result.getId());
        assertEquals(testUserDto.getEmail(), result.getEmail());
        assertEquals(testUserDto.getUsername(), result.getUsername());
        verify(userRepository).findById(1L);
        verify(usersMapper).toDto(testUser);
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID")
    void shouldThrowExceptionWhenUserNotFoundById() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.getUsersById(999L));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(999L);
        verify(usersMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Arrange
        UsersDto updateDto = new UsersDto();
        updateDto.setUsername("updateduser");
        updateDto.setEmail("updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(Users.class))).thenReturn(testUser);
        when(usersMapper.toDto(testUser)).thenReturn(updateDto);

        // Act
        UsersDto result = userService.updateUser(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("updateduser", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
        verify(userRepository).findById(1L);
        verify(userRepository).save(testUser);
        verify(usersMapper).toDto(testUser);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        // Arrange
        UsersDto updateDto = new UsersDto();
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.updateUser(999L, updateDto));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should login user successfully with valid credentials")
    void shouldLoginUserSuccessfullyWithValidCredentials() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com", 1L)).thenReturn("jwtToken");
        when(usersMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        LoginResponseDto result = userService.login("test@example.com", "password123");

        // Assert
        assertNotNull(result);
        assertEquals("jwtToken", result.getToken());
        assertEquals(testUserDto, result.getUser());
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtil).generateToken("test@example.com", 1L);
        verify(usersMapper).toDto(testUser);
    }

    @Test
    @DisplayName("Should throw exception when user not found during login")
    void shouldThrowExceptionWhenUserNotFoundDuringLogin() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.login("nonexistent@example.com", "password123"));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByEmail("nonexistent@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), any());
    }

    @Test
    @DisplayName("Should throw exception when password is invalid during login")
    void shouldThrowExceptionWhenPasswordIsInvalidDuringLogin() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.login("test@example.com", "wrongpassword"));
        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("wrongpassword", "encodedPassword");
        verify(jwtUtil, never()).generateToken(anyString(), any());
    }
}
