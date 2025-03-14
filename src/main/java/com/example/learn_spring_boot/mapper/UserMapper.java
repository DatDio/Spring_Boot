package com.example.learn_spring_boot.mapper;

import com.example.learn_spring_boot.dtos.requests.users.CreateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UpdateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UserDto;
import com.example.learn_spring_boot.entities.Users;
import com.example.learn_spring_boot.enums.Gender;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserMapper {

    public UserDto toDto(Users user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setPassWord(user.getPassWord());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender() != null ? user.getGender().name() : null);
        return dto;
    }

    public Users toEntity(CreateUserRequest request) {
        if (request == null) {
            return null;
        }
        Users user = new Users();
        user.setUserName(request.getUserName());
        user.setPassWord(request.getPassWord());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(Gender.valueOf(request.getGender())); // Convert String -> Enum
        return user;
    }

    public void updateUserFromDto(UpdateUserRequest request, Users user) {
        if (request == null || user == null) {
            return;
        }
        if (StringUtils.hasText(request.getUserName())) {
            user.setUserName(request.getUserName());
        }
        if (StringUtils.hasText(request.getPassWord())) {
            user.setPassWord(request.getPassWord());
        }
        if (StringUtils.hasText(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        if (StringUtils.hasText(request.getPhoneNumber())) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
        if (StringUtils.hasText(request.getGender())) {
            user.setGender(Gender.valueOf(request.getGender())); // Convert String -> Enum
        }
    }
}


