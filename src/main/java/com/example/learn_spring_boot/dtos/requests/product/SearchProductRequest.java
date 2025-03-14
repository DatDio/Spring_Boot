package com.example.learn_spring_boot.dtos.requests.product;

import com.example.learn_spring_boot.SystemContants.PaginationConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchProductRequest {
    private Long id;
    private long price;

    private String name;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdTo;

    private String sortBy = "createAt";

    private String direction="asc";

    private  int page = PaginationConstant.DEFAULT_PAGE;

    private  int size = PaginationConstant.DEFAULT_SIZE;
}
