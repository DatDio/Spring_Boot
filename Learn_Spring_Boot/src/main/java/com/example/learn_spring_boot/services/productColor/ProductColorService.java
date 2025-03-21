package com.example.learn_spring_boot.services.productColor;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.productColor.CreateProductColorRequest;
import com.example.learn_spring_boot.dtos.productColor.ProductColorDto;
import com.example.learn_spring_boot.entities.Products;

import java.util.List;

public interface ProductColorService {
    ApiResponse<ProductColorDto> createProductColor(CreateProductColorRequest request, Products product);
    ApiResponse<List<ProductColorDto>> getByProductId(Long productId);
}
