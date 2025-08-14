package com.example.productivity_app.service;

import com.example.productivity_app.dto.LoginResponseDto;
import com.example.productivity_app.dto.RegisterDto;
import com.example.productivity_app.dto.UsersDto;
import com.example.productivity_app.entity.Users;
import com.example.productivity_app.mapper.UsersMapper;
import com.example.productivity_app.repository.UserRepository;
import com.example.productivity_app.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       UsersMapper usersMapper,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.usersMapper = usersMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UsersDto getUsersById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return usersMapper.toDto(user);
    }

    public UsersDto createUser(RegisterDto registerDto) {
        Users user = new Users();
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Users saved = userRepository.save(user);
        return usersMapper.toDto(saved);
    }

    public UsersDto updateUser(Long id, UsersDto usersDto) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(usersDto.getUsername());
        existingUser.setEmail(usersDto.getEmail());
        // optional: existingUser.setPassword(usersDto.getPassword());

        Users updated = userRepository.save(existingUser);
        return usersMapper.toDto(updated);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public LoginResponseDto login(String email, String password) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        UsersDto userDto = usersMapper.toDto(user);
        
        return new LoginResponseDto(token, userDto);
    }
}
