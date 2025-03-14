package com.example.learn_spring_boot.mapper;

import com.example.learn_spring_boot.dtos.requests.product.CreateProductRequest;
import com.example.learn_spring_boot.dtos.requests.product.ProductDto;
import com.example.learn_spring_boot.dtos.requests.product.UpdateProductRequest;
import com.example.learn_spring_boot.entities.Products;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Products product) {
        if (product == null) {
            return null;
        }
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setPrice(product.getPrice());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());
        return dto;
    }

    public Products toEntity(CreateProductRequest request) {
        if (request == null) {
            return null;
        }
        Products product = new Products();
        product.setPrice(request.getPrice());
        product.setName(request.getName());
        product.setDescription(request.getDescription());

        // Không xử lý lưu ảnh, chỉ đặt imageUrl là null hoặc mặc định
        product.setImageUrl(null);

        return product;
    }

    public void updateProductFromDto(UpdateProductRequest request, Products product) {
        if (request == null || product == null) {
            return;
        }
        if (request.getPrice() > 0) {
            product.setPrice(request.getPrice());
        }
        if (request.getName() != null && !request.getName().isEmpty()) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            product.setDescription(request.getDescription());
        }

        // Không xử lý cập nhật ảnh
    }
}
