package com.example.learn_spring_boot.dtos.product;

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
    private long priceFrom;
    private long priceTo;
    private String name;
    private String description;
    private Long brandId;
    private Long categoryId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdTo;

    private String sortBy = "createAt";

    private String direction="asc";

    private  int page = PaginationConstant.DEFAULT_PAGE;

    private  int size = PaginationConstant.DEFAULT_SIZE;
}
