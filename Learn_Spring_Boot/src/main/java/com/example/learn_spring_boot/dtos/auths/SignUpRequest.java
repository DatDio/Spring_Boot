package com.example.learn_spring_boot.dtos.auths;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "username trống")
    private String userName;

    @NotBlank(message = "Email trống")
    private String email;

    @NotBlank(message = "Mật khẩu trống")
//    @Pattern(regexp = "^(?=.*[0-9])(.{8,})$", message = "Mật khẩu phải có ít nhất 8 ký tự và chứa ít nhất 1 số")
    private String password;

   // private AccountRoles role;

    private String phoneNumber;
}
