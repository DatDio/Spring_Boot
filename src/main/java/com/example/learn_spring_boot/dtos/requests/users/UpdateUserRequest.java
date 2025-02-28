package com.example.learn_spring_boot.dtos.requests.users;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String userName;
    private String passWord;
    private String email;
    private String phoneNumber;
}
