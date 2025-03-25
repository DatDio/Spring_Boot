package com.example.learn_spring_boot.controllers.admin;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.Utils.PageableObject;
import com.example.learn_spring_boot.dtos.product.CreateProductRequest;
import com.example.learn_spring_boot.dtos.product.ProductDto;
import com.example.learn_spring_boot.dtos.product.SearchProductRequest;
import com.example.learn_spring_boot.dtos.product.UpdateProductRequest;
import com.example.learn_spring_boot.services.products.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create")
    public ApiResponse<ProductDto> createProduct(@RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }


    // Cập nhật Product
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/update")
    public ApiResponse<ProductDto> updateProduct( @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(request);
    }

    // Xóa User
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public ApiResponse<String> deleteProduct(@RequestParam Long id) {

        return productService.deleteProduct(id);
    }


    @PostMapping("/search")
    public ApiResponse<PageableObject<ProductDto>> searchProduct(@RequestBody SearchProductRequest request) {

        return productService.searchProducts(request);
    }
}
