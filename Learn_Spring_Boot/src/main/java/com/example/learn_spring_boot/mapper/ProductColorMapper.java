package com.example.learn_spring_boot.mapper;

import com.example.learn_spring_boot.dtos.productColor.CreateProductColorRequest;
import com.example.learn_spring_boot.dtos.productColor.ProductColorDto;
import com.example.learn_spring_boot.dtos.productColor.UpdateProductColorRequest;
import com.example.learn_spring_boot.entities.ProductColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductColorMapper {

    @Autowired
    private ProductVariantMapper productVariantMapper;

    public ProductColorDto toDto(ProductColor productColor) {
        if (productColor == null) return null;

        return new ProductColorDto(
                productColor.getProduct().getId(),
                productColor.getPrice(),
                productColor.getColor(),
                productColor.getImageUrl(),
                productColor.getVariants().stream()
                        .map(productVariantMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    public ProductColor toEntity(CreateProductColorRequest request) {
        if (request == null) return null;

        ProductColor productColor = new ProductColor();
        productColor.setPrice(request.getPrice());
        productColor.setColor(request.getColor());
        //productColor.setImageUrl(request.getImageUrl());
        return productColor;
    }

    public void updateEntity(UpdateProductColorRequest request, ProductColor productColor) {
        if (request == null || productColor == null) return;

        productColor.setPrice(request.getPrice());
        productColor.setColor(request.getColor());
        productColor.setImageUrl(request.getImageUrl());
    }
}