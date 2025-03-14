package com.example.learn_spring_boot.controllers;

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
import com.example.learn_spring_boot.services.products.ProductService;
import com.example.learn_spring_boot.services.users.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductDto> createProduct(@Valid @ModelAttribute CreateProductRequest request) {
        return productService.createProduct(request);
    }

    // Cập nhật User
    @PostMapping("/update")
    public ApiResponse<ProductDto> updateUser(@RequestParam Long id, @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(id, request);
    }

    // Xóa User
    @PostMapping("/delete")
    public ApiResponse<String> deleteUser(@RequestParam Long id) {
        return productService.deleteProduct(id);
    }


    @PostMapping("/search")
    public ApiResponse<PageableObject<ProductDto>> searchUsers(@RequestBody SearchProductRequest request) {

        return productService.searchProducts(request);
    }
}
