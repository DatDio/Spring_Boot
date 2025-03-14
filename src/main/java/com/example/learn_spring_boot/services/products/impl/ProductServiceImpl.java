package com.example.learn_spring_boot.services.products.impl;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.Utils.PageableObject;
import com.example.learn_spring_boot.dtos.requests.product.CreateProductRequest;
import com.example.learn_spring_boot.dtos.requests.product.ProductDto;
import com.example.learn_spring_boot.dtos.requests.product.SearchProductRequest;
import com.example.learn_spring_boot.dtos.requests.product.UpdateProductRequest;
import com.example.learn_spring_boot.entities.Products;
import com.example.learn_spring_boot.mapper.ProductMapper;
import com.example.learn_spring_boot.repositories.products.IProductRepository;
import com.example.learn_spring_boot.services.FileStorage.FileStorageService;
import com.example.learn_spring_boot.services.products.ProductService;
import com.example.learn_spring_boot.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final IProductRepository productRepository;
    private final ProductMapper productMapper;
     private final FileStorageService fileStorageService;
    public ProductServiceImpl(IProductRepository productRepository,
                              ProductMapper productMapper,
                              FileStorageService fileStorageService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public ApiResponse<ProductDto> createProduct(CreateProductRequest request) {
        Products product = productMapper.toEntity(request);
        var imageurl= fileStorageService.saveImage(request.getImage());
        if(StringUtils.isEmpty(imageurl)){
            return ApiResponse.failure("Something went wrong!Cannot upload image");
        }
        product.setImageUrl(imageurl);
        Products savedProduct = productRepository.save(product);
        return ApiResponse.success("Product created successfully!", productMapper.toDto(savedProduct));
    }

    @Override
    @Transactional
    public ApiResponse<ProductDto> updateProduct(Long id, UpdateProductRequest productDetails) {
        return productRepository.findById(id).map(product -> {
            productMapper.updateProductFromDto(productDetails, product);
            productRepository.save(product);
            return ApiResponse.success("Product updated successfully!", productMapper.toDto(product));
        }).orElseGet(() -> ApiResponse.failure("Product not found!"));
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteProduct(Long id) {
        var product = productRepository.findById(id)
                .orElse(null);

        if (product == null) {
            return ApiResponse.failure("Product not found!");
        }

        String imageUrl = product.getImageUrl();

        // Xóa sản phẩm khỏi database
        productRepository.deleteById(id);

        // Nếu sản phẩm có ảnh, thì xóa ảnh
        boolean isDeleted = fileStorageService.deleteImage(imageUrl);

        return isDeleted
                ? ApiResponse.success("Product and image deleted successfully!", null)
                : ApiResponse.success("Product deleted, but image not found!", null);
    }


    @Override
    public ApiResponse<PageableObject<ProductDto>> searchProducts(SearchProductRequest request) {
        try {
            Specification<Products> spec = ProductSpecification.filterProducts(request);

            String sortBy = request.getSortBy() != null ? request.getSortBy() : "createAt";
            String direction = request.getDirection() != null ? request.getDirection() : "asc";

            Sort sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(Math.max(request.getPage() - 1, 0), request.getSize(), sort);

            Page<Products> productPage = productRepository.findAll(spec, pageable);
            List<ProductDto> productDtos = productPage.getContent().stream().map(productMapper::toDto).collect(Collectors.toList());

            PageableObject<ProductDto> response = new PageableObject<>(
                    productDtos, productPage.hasNext(), productPage.getTotalPages(), productPage.getNumber() + 1
            );
            return ApiResponse.success("Fetched products successfully!", response);
        } catch (Exception e) {
            return ApiResponse.failure(e.getMessage());
        }
    }
}