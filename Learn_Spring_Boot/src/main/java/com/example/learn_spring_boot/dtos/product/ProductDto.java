package com.example.learn_spring_boot.dtos.product;

import com.example.learn_spring_boot.dtos.base.BaseDto;
import com.example.learn_spring_boot.dtos.productColor.ProductColorDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto extends BaseDto {

    private String name;
    private String description;
    private String imageUrl;
    private Long brandId;
    private Long categoryId;

    private List<ProductColorDto> productColors;
}
