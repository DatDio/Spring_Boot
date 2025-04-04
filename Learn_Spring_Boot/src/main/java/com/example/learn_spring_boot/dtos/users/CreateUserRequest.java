package com.example.learn_spring_boot.dtos.users;

import com.example.learn_spring_boot.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CreateUserRequest {

   //@NotBlank(message = "UserName không được để trống")
   //@Size(min = 3, max = 20, message = "Username phải có từ 3 đến 20 ký tự")
    private String userName;


    private String passWord;

//    @NotBlank(message = "Email không được để trống")
//    @Email(message = "Email không hợp lệ")
    private String email;

    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
    private String gender = Gender.MALE.name();
}
