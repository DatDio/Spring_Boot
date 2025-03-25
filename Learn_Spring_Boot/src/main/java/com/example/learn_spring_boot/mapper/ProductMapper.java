package com.example.learn_spring_boot.mapper;

import com.example.learn_spring_boot.dtos.product.CreateProductRequest;
import com.example.learn_spring_boot.dtos.product.ProductDto;
import com.example.learn_spring_boot.dtos.product.UpdateProductRequest;
import com.example.learn_spring_boot.entities.Brand;
import com.example.learn_spring_boot.entities.Category;
import com.example.learn_spring_boot.entities.ProductColor;
import com.example.learn_spring_boot.entities.Products;
import com.example.learn_spring_boot.repositories.brands.BrandRepository;
import com.example.learn_spring_boot.repositories.category.CategoryRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.util.StringUtils;

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
        dto.setBrandId(product.getBrand().getId());
        dto.setCategoryId(product.getCategory().getId());
        if (product.getProductColors() != null) {
            dto.setProductColors(
                    product.getProductColors().stream()
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

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found!"));
        product.setBrand(brand);


        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found!"));
        product.setCategory(category);
// ✅ Dùng mapper để convert danh sách ProductColor
        if (request.getProductColors() != null) {
            Set<ProductColor> colors = request.getProductColors().stream()
                    .map(colorRequest -> productColorMapper.toEntity(colorRequest, product))
                    .collect(Collectors.toSet());
            product.setProductColors(colors);
        }
        return product;
    }

    public void updateProductFromDto(UpdateProductRequest request, Products product) {
        if (request == null || product == null) {
            return;
        }

        if (StringUtils.hasText(request.getName()) && product.getName() != request.getName()) {
            product.setName(request.getName());
        }
        if (StringUtils.hasText(request.getDescription())) {
            product.setDescription(request.getDescription());
        }
        if (StringUtils.hasText(request.getImageUrl())) {
            product.setImageUrl(request.getImageUrl());
        }

        // Cập nhật Brand nếu có
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Brand not found!"));
            product.setBrand(brand);
        }

        // Cập nhật Category nếu có
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found!"));
            product.setCategory(category);
        }

        // Cập nhật danh sách ProductColor nếu có
//        if (request.getProductColors() != null) {
//            Set<ProductColor> colors = request.getProductColors().stream()
//                    .map(colorRequest -> productColorMapper.Upda(colorRequest, product))
//                    .collect(Collectors.toSet());
//            product.setProductColors(colors);
//        }
    }

}
