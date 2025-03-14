package com.example.learn_spring_boot.dtos.requests.product;

import com.example.learn_spring_boot.dtos.requests.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto extends BaseDto {
    private long price;

    private String name;
    private String description;

    private String imageUrl;
}
