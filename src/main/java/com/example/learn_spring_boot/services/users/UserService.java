package com.example.learn_spring_boot.services.users;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.Utils.PageableObject;
import com.example.learn_spring_boot.dtos.requests.users.CreateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.SearchUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UpdateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UserDto;
import com.example.learn_spring_boot.entities.Users;
import com.example.learn_spring_boot.mapper.UserMapper;
import com.example.learn_spring_boot.repositories.IUserRepository;
import com.example.learn_spring_boot.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

public interface UserService {
    ApiResponse<UserDto> createUser(CreateUserRequest request);
    ApiResponse<UserDto> updateUser(Long id, UpdateUserRequest userDetails);
    ApiResponse<String> deleteUser(Long id);
    ApiResponse<PageableObject<UserDto>> searchUsers(SearchUserRequest request);
}
