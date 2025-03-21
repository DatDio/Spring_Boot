package com.example.learn_spring_boot.dtos.productColor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductColorRequest {
    private BigDecimal price; // Giá mới của màu sản phẩm
    private String color;     // Màu sắc mới của sản phẩm
    private String imageUrl;
}
