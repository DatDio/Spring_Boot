package com.example.learn_spring_boot.sercurity.auth;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RefreshTokenRequets {
    private String token;
}
