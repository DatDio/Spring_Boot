package com.example.learn_spring_boot.dtos.requests.users;

import com.example.learn_spring_boot.SystemContants.PaginationConstant;
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
    private LocalDate createdFrom;
    private LocalDate createdTo;
    private String sortBy = "createAt";
    private String direction="asc";
    private  int page = PaginationConstant.DEFAULT_PAGE;
    private  int size = PaginationConstant.DEFAULT_SIZE;
}
