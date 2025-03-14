package com.example.learn_spring_boot.services.products;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.Utils.PageableObject;
import com.example.learn_spring_boot.dtos.requests.product.CreateProductRequest;
import com.example.learn_spring_boot.dtos.requests.product.ProductDto;
import com.example.learn_spring_boot.dtos.requests.product.SearchProductRequest;
import com.example.learn_spring_boot.dtos.requests.product.UpdateProductRequest;
import com.example.learn_spring_boot.dtos.requests.users.CreateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.SearchUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UpdateUserRequest;
import com.example.learn_spring_boot.dtos.requests.users.UserDto;

public interface ProductService {
    ApiResponse<ProductDto> createProduct(CreateProductRequest request);
    ApiResponse<ProductDto> updateProduct(Long id, UpdateProductRequest productDetails);
    ApiResponse<String> deleteProduct(Long id);
    ApiResponse<PageableObject<ProductDto>> searchProducts(SearchProductRequest request);
}
