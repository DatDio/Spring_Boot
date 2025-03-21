package com.example.learn_spring_boot.mapper;

import com.example.learn_spring_boot.dtos.product.CreateProductRequest;
import com.example.learn_spring_boot.dtos.product.ProductDto;
import com.example.learn_spring_boot.dtos.product.UpdateProductRequest;
import com.example.learn_spring_boot.entities.Brand;
import com.example.learn_spring_boot.entities.Category;
import com.example.learn_spring_boot.entities.Products;
import com.example.learn_spring_boot.repositories.brands.BrandRepository;
import com.example.learn_spring_boot.repositories.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private ProductColorMapper productColorMapper;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public ProductDto toDto(Products product) {
        if (product == null) {
            return null;
        }
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());

        // ✅ Dùng ProductColorMapper để convert danh sách ProductColor
        if (product.getColors() != null) {
            dto.setProductColors(
                    product.getColors().stream()
                            .map(productColorMapper::toDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public Products toEntity(CreateProductRequest request) {
        if (request == null) {
            return null;
        }

        Products product = new Products();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        // ✅ Lấy Brand từ database
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found!"));
        product.setBrand(brand);

        // ✅ Lấy Category từ database
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found!"));
        product.setCategory(category);

        return product;
    }

    public void updateProductFromDto(UpdateProductRequest request, Products product) {
        if (request == null || product == null) {
            return;
        }

        if (request.getName() != null && !request.getName().isEmpty()) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            product.setDescription(request.getDescription());
        }

        // ✅ Cập nhật ảnh nếu có
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            product.setImageUrl(request.getImageUrl());
        }
    }
}
