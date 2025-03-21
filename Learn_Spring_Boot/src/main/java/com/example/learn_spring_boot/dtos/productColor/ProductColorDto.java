package com.example.learn_spring_boot.dtos.productColor;

import com.example.learn_spring_boot.dtos.base.BaseDto;
import com.example.learn_spring_boot.dtos.productvariant.ProductVariantDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductColorDto extends BaseDto {
    private Long productId;      // ID của sản phẩm liên kết
    private BigDecimal price;    // Giá của màu sản phẩm
    private String color;        // Màu sắc
    private String imageUrl;
    private List<ProductVariantDto> variants;
}
