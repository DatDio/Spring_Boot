package com.example.learn_spring_boot.dtos.productvariant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductVariantRequest {
    private Long productColorId;
    private String productSize;
    private int stockQuantity;
}
