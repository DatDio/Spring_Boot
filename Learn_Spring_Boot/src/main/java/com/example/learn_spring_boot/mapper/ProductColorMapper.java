package com.example.learn_spring_boot.mapper;

import com.example.learn_spring_boot.dtos.productColor.CreateProductColorRequest;
import com.example.learn_spring_boot.dtos.productColor.ProductColorDto;
import com.example.learn_spring_boot.dtos.productColor.UpdateProductColorRequest;
import com.example.learn_spring_boot.dtos.productvariant.ProductVariantDto;
import com.example.learn_spring_boot.entities.ProductColor;
import com.example.learn_spring_boot.entities.ProductVariant;
import com.example.learn_spring_boot.entities.Products;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductColorMapper {

    @Autowired
    private ProductVariantMapper productVariantMapper;

    public ProductColorDto toDto(ProductColor productColor) {
        if (productColor == null) return null;

        return ProductColorDto.builder()
                .id(productColor.getId()) // ✅ Gán ID từ BaseDto
                //.productId(productColor.getProduct().getId())
                .price(productColor.getPrice())
                .color(productColor.getColor())
                .imageUrl(productColor.getImageUrl())
                .variants(productColor.getVariants().stream()
                        .map(variant -> {
                            ProductVariantDto variantDto = productVariantMapper.toDto(variant);
                            //variantDto.setProductColorId(productColor.getId()); // ✅ Gán productColorId
                            return variantDto;
                        })
                        .collect(Collectors.toList()))
                .build(); // ✅ Dùng builder
    }

    public ProductColor toEntity(CreateProductColorRequest request, Products product) {
        if (request == null) {
            return null;
        }

        ProductColor color = new ProductColor();
        color.setProduct(product);
        color.setPrice(request.getPrice());
        color.setColor(request.getColor());
        color.setImageUrl(request.getImageUrl());

        if (request.getVariants() != null) {
            Set<ProductVariant> variants = request.getVariants().stream()
                    .map(variantRequest -> productVariantMapper.toEntity(variantRequest, color))
                    .collect(Collectors.toSet());
            color.setVariants(variants);
        }

        return color;
    }

    public void updateProductColorFromDto(UpdateProductColorRequest request, ProductColor productColor) {
        if (request == null || productColor == null) return;

        if (request.getPrice() > 0) {
            productColor.setPrice(request.getPrice());
        }
        if (StringUtils.hasText(request.getColor())) {
            productColor.setColor(request.getColor());
        }
        if (StringUtils.hasText(request.getImageUrl())) {
            productColor.setImageUrl(request.getImageUrl());
        }
    }

}
