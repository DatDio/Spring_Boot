package com.example.learn_spring_boot.controllers;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.requests.users.CreateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UpdateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UserDto;
import com.example.learn_spring_boot.entities.Users;
import com.example.learn_spring_boot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // Lấy danh sách tất cả User
    @PostMapping("/getAll")
    public ApiResponse<List<UserDto>> getAllUsers() {
        return userService.getAllUsers();
    }

    // Lấy thông tin User theo ID
    @PostMapping("/getById")
    public ApiResponse<UserDto> getUserById(@RequestParam Long id) {
        return userService.getUserById(id);
    }

    // Tạo User mới
    @PostMapping("/create")
    public ApiResponse<UserDto> createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    // Cập nhật User
    @PostMapping("/update")
    public ApiResponse<UserDto> updateUser(@RequestParam Long id, @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    // Xóa User
    @PostMapping("/delete")
    public ApiResponse<String> deleteUser(@RequestParam Long id) {
        return userService.deleteUser(id);
    }

}
