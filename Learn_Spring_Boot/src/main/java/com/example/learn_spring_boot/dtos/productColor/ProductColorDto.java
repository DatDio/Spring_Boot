package com.example.learn_spring_boot.dtos.productColor;

import com.example.learn_spring_boot.dtos.base.BaseDto;
import com.example.learn_spring_boot.dtos.productvariant.ProductVariantDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class ProductColorDto extends BaseDto {
    //private Long productId;      // ID của sản phẩm liên kết
    private double price;
    private String color;
    private String imageUrl;
    private List<ProductVariantDto> variants;
}
