package com.example.learn_spring_boot.sercurity.auth;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtAuhenticationResponse {

    private String token;

    private String refreshToken;
}
