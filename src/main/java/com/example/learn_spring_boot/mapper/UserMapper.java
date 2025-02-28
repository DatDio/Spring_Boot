package com.example.learn_spring_boot.mapper;

import com.example.learn_spring_boot.dtos.requests.users.CreateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UpdateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UserDto;
import com.example.learn_spring_boot.entities.Users;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Map từ Users sang UserDto
    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "passWord", target = "passWord")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    UserDto toDto(Users user);

    // Map từ CreateUserRequest sang Users
    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "passWord", target = "passWord")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    Users toEntity(CreateUserRequest request);

    // Map từ UpdateUserRequest sang Users (Cập nhật từng trường, bỏ qua giá trị null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserRequest request, @MappingTarget Users user);
}

