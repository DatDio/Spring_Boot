package com.example.learn_spring_boot.dtos.productvariant;

import com.example.learn_spring_boot.dtos.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductVariantDto extends BaseDto {
   // private Long productColorId; // ID của ProductColor
    private String productSize;
    private int stockQuantity;
}
