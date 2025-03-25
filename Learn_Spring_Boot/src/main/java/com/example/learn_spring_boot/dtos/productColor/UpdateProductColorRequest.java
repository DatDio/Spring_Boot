package com.example.learn_spring_boot.dtos.productColor;

import com.example.learn_spring_boot.dtos.base.BaseDto;
import com.example.learn_spring_boot.dtos.productvariant.ProductVariantDto;
import com.example.learn_spring_boot.dtos.productvariant.UpdateProductVariantRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductColorRequest extends BaseDto {
    @NotBlank(message = "Giá không được để trống!")
    private double price;
    private String color;
    @NotBlank(message = "Ảnh không được để trống!")
    private String imageUrl;
    private Set<UpdateProductVariantRequest> variants;
}
