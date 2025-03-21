package com.example.learn_spring_boot.mapper;

import com.example.learn_spring_boot.dtos.productvariant.CreateProductVariantRequest;
import com.example.learn_spring_boot.dtos.productvariant.ProductVariantDto;
import com.example.learn_spring_boot.dtos.productvariant.UpdateProductVariantRequest;
import com.example.learn_spring_boot.entities.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class ProductVariantMapper {

    public ProductVariantDto toDto(ProductVariant variant) {
        if (variant == null) {
            return null;
        }
        ProductVariantDto dto = new ProductVariantDto();
        dto.setId(variant.getId());
        dto.setProductSize(variant.getProductsize());
        dto.setStockQuantity(variant.getStockQuantity());
        return dto;
    }

    public ProductVariant toEntity(CreateProductVariantRequest request) {
        if (request == null) {
            return null;
        }
        ProductVariant variant = new ProductVariant();
        variant.setProductsize(request.getProductSize());
        variant.setStockQuantity(request.getStockQuantity());
        return variant;
    }

    public void updateProductVariantFromDto(UpdateProductVariantRequest request, ProductVariant variant) {
        if (request == null || variant == null) {
            return;
        }
        if (request.getProductSize() != null && !request.getProductSize().isEmpty()) {
            variant.setProductsize(request.getProductSize());
        }
        if (request.getStockQuantity() > 0) {
            variant.setStockQuantity(request.getStockQuantity());
        }
    }
}