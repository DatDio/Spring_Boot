package com.example.learn_spring_boot.dtos.users;

import com.example.learn_spring_boot.SystemContants.PaginationConstant;
import com.example.learn_spring_boot.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserRequest {
    private Long id;
    private  String userName;
    private  String email;
    private  String phoneNumber;
    private  String gender ;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirthFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirthTo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdTo;

    private String sortBy = "createAt";

    private String direction="asc";

    private  int page = PaginationConstant.DEFAULT_PAGE;

    private  int size = PaginationConstant.DEFAULT_SIZE;
}
