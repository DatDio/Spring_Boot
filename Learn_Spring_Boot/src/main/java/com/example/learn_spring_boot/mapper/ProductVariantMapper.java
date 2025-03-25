package com.example.learn_spring_boot.mapper;

import com.example.learn_spring_boot.dtos.productvariant.CreateProductVariantRequest;
import com.example.learn_spring_boot.dtos.productvariant.ProductVariantDto;
import com.example.learn_spring_boot.dtos.productvariant.UpdateProductVariantRequest;
import com.example.learn_spring_boot.entities.ProductColor;
import com.example.learn_spring_boot.entities.ProductVariant;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ProductVariantMapper {

    public ProductVariantDto toDto(ProductVariant variant) {
        if (variant == null) {
            return null;
        }
        ProductVariantDto dto = new ProductVariantDto();
        //dto.setProductColorId(variant.getProductColor().getId());
        dto.setId(variant.getId());
        dto.setProductSize(variant.getProductSize());
        dto.setStockQuantity(variant.getStockQuantity());
        return dto;
    }

    public ProductVariant toEntity(CreateProductVariantRequest request, ProductColor productColor) {
        if (request == null) {
            return null;
        }

        ProductVariant variant = new ProductVariant();
        variant.setProductColor(productColor);
        variant.setProductSize(request.getProductSize());
        variant.setStockQuantity(request.getStockQuantity());

        return variant;
    }

    public void updateProductVariantFromDto(UpdateProductVariantRequest request, ProductVariant variant) {
        if (request == null || variant == null) {
            return;
        }
        if (StringUtils.hasText(request.getProductSize())) {
            variant.setProductSize(request.getProductSize());
        }
        if (request.getStockQuantity() >= 0) {
            variant.setStockQuantity(request.getStockQuantity());
        }
    }

}