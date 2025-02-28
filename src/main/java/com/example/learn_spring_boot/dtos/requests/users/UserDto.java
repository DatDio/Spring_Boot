package com.example.learn_spring_boot.dtos.requests.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String passWord;
    private String email;
    private String phoneNumber;
    //private String refreshToken;
}
