package com.example.learn_spring_boot.services.users;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.Utils.PageableObject;
import com.example.learn_spring_boot.dtos.requests.users.CreateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.SearchUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UpdateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UserDto;

import java.util.List;

public interface UserService {
    ApiResponse<UserDto> createUser(CreateUserRequest request);
    ApiResponse<UserDto> updateUser(Long id, UpdateUserRequest userDetails);
    ApiResponse<String> deleteUser(Long id);
    ApiResponse<PageableObject<UserDto>> searchUsers(SearchUserRequest request);
    ApiResponse<PageableObject<UserDto>> searchUsersNativeQuery(SearchUserRequest request);
   ApiResponse<PageableObject<UserDto>> searchUsersProcedure(SearchUserRequest request);
        List<UserDto> searchAllUsersProcedure();
}
