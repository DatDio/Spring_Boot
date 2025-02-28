package com.example.learn_spring_boot.services;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.requests.users.CreateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UpdateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UserDto;
import com.example.learn_spring_boot.entities.Users;
import com.example.learn_spring_boot.mapper.UserMapper;
import com.example.learn_spring_boot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private  final UserRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // Lấy tất cả users, trả về danh sách UserDto
    public ApiResponse<List<UserDto>> getAllUsers() {
        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ApiResponse.success("Fetched users successfully!", users);
    }
    // Lấy User theo ID
    public ApiResponse<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> ApiResponse.success("User found!", userMapper.toDto(user)))
                .orElseGet(() -> ApiResponse.failure("User not found!"));
    }

    // Tạo User mới
    public ApiResponse<UserDto> createUser(CreateUserRequest request) {
        Users user = userMapper.toEntity(request);
        Users savedUser = userRepository.save(user);
        return ApiResponse.success("User created successfully!", userMapper.toDto(savedUser));
    }

    // Cập nhật User
    public ApiResponse<UserDto> updateUser(Long id, UpdateUserRequest userDetails) {
        return userRepository.findById(id).map(user -> {
            userMapper.updateUserFromDto(userDetails, user);
            userRepository.save(user);
            return ApiResponse.success("User updated successfully!", userMapper.toDto(user));
        }).orElseGet(() -> ApiResponse.failure("User not found!"));
    }

    // Xóa User theo ID
    public ApiResponse<String> deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ApiResponse.success("User deleted successfully!", "User ID: " + id);
        }
        return ApiResponse.failure("User not found!");
    }







}
