package com.example.learn_spring_boot.services.products;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.Utils.PageableObject;
import com.example.learn_spring_boot.dtos.product.CreateProductRequest;
import com.example.learn_spring_boot.dtos.product.ProductDto;
import com.example.learn_spring_boot.dtos.product.SearchProductRequest;
import com.example.learn_spring_boot.dtos.product.UpdateProductRequest;

public interface ProductService {
    ApiResponse<ProductDto> createProduct(CreateProductRequest request);
    ApiResponse<ProductDto> updateProduct( UpdateProductRequest productDetails);
    ApiResponse<String> deleteProduct(Long id);
    ApiResponse<PageableObject<ProductDto>> searchProducts(SearchProductRequest request);
}
