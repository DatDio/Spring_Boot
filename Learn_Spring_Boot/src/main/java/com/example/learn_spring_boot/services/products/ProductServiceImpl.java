package com.example.learn_spring_boot.services.products;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.Utils.PageableObject;
import com.example.learn_spring_boot.dtos.product.CreateProductRequest;
import com.example.learn_spring_boot.dtos.product.ProductDto;
import com.example.learn_spring_boot.dtos.product.SearchProductRequest;
import com.example.learn_spring_boot.dtos.product.UpdateProductRequest;
import com.example.learn_spring_boot.dtos.productColor.UpdateProductColorRequest;
import com.example.learn_spring_boot.dtos.productvariant.UpdateProductVariantRequest;
import com.example.learn_spring_boot.entities.*;
import com.example.learn_spring_boot.mapper.ProductColorMapper;
import com.example.learn_spring_boot.mapper.ProductMapper;
import com.example.learn_spring_boot.repositories.brands.BrandRepository;
import com.example.learn_spring_boot.repositories.category.CategoryRepository;
import com.example.learn_spring_boot.repositories.products.ProductRepository;
import com.example.learn_spring_boot.services.FileStorage.FileStorageService;
import com.example.learn_spring_boot.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductColorMapper productColorMapper;
    private final FileStorageService fileStorageService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper,
                              ProductColorMapper productColorMapper,
                              FileStorageService fileStorageService,
                              CategoryRepository categoryRepository,
                              BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productColorMapper = productColorMapper;
        this.fileStorageService = fileStorageService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    @Transactional
    public ApiResponse<ProductDto> createProduct(CreateProductRequest request) {
        try {
            // Lấy thông tin Brand & Category
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found"));


            Products product = productMapper.toEntity(request);

            Products savedProduct = productRepository.save(product);
            return ApiResponse.success("Product created successfully!", productMapper.toDto(savedProduct));
        } catch (Exception e) {
            return ApiResponse.failure("Failed to create product: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public ApiResponse<ProductDto> updateProduct(UpdateProductRequest request) {
        try {
            // 1️⃣ Kiểm tra sản phẩm có tồn tại không
            Products existingProduct = productRepository.findById(request.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            if (request.getName() != null && !request.getName().equals(existingProduct.getName())) {

                existingProduct.setName(request.getName());
            }
            if(request.getImageUrl() !=null && !request.getImageUrl().equals(existingProduct.getImageUrl())) {
                fileStorageService.deleteImage(request.getImageUrl());
                existingProduct.setImageUrl(request.getImageUrl());
            }
            if (request.getDescription() != null && !request.getDescription().equals(existingProduct.getDescription())) {
                existingProduct.setDescription(request.getDescription());
            }

            if (request.getBrandId() != null && (existingProduct.getBrand() == null || !existingProduct.getBrand().getId().equals(request.getBrandId()))) {
                Brand brand = brandRepository.findById(request.getBrandId())
                        .orElseThrow(() -> new RuntimeException("Brand not found!"));
                existingProduct.setBrand(brand);
            }
            if (request.getCategoryId() != null && (existingProduct.getCategory() == null || !existingProduct.getCategory().getId().equals(request.getCategoryId()))) {
                Category category = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found!"));
                existingProduct.setCategory(category);
            }

            // Xóa toàn bộ ProductColor & ProductVariant
            //existingProduct.getProductColors().clear();
            updateProductColors(request.getProductColors(), existingProduct);

            Products savedProduct = productRepository.save(existingProduct);

            return ApiResponse.success("Product updated successfully!", productMapper.toDto(savedProduct));
        } catch (Exception e) {
            return ApiResponse.failure("Failed to update product: " + e.getMessage());
        }
    }

    private void updateProductColorsByDeleteAllOldProductColor(Set<UpdateProductColorRequest> colorRequests, Products product) {
        // 1️⃣ Xóa toàn bộ ProductColor & ProductVariant
        for (ProductColor color : product.getProductColors()) {
            // Xóa ảnh nếu có
            fileStorageService.deleteImage(color.getImageUrl());
            // Xóa toàn bộ ProductVariant của ProductColor
            color.getVariants().clear();
        }
        product.getProductColors().clear();

        // 2️⃣ Thêm mới ProductColor từ request
        Set<ProductColor> newColors = new HashSet<>();
        for (UpdateProductColorRequest colorRequest : colorRequests) {
            ProductColor color = new ProductColor();
            color.setProduct(product);
            color.setPrice(colorRequest.getPrice());
            color.setColor(colorRequest.getColor());
            color.setImageUrl(colorRequest.getImageUrl());

            // 3️⃣ Cập nhật ProductVariant nếu có
            if (colorRequest.getVariants() != null) {
                updateProductVariants(colorRequest.getVariants(), color);
            }

            newColors.add(color);
        }

        product.getProductColors().addAll(newColors);
    }
    private void updateProductVariantsByDeleteOldProductVariant(Set<UpdateProductVariantRequest> variantRequests, ProductColor productColor) {
        // Xóa hết ProductVariant cũ
        productColor.getVariants().clear();

        // Thêm mới danh sách ProductVariant từ request
        Set<ProductVariant> newVariants = new HashSet<>();
        for (UpdateProductVariantRequest variantRequest : variantRequests) {
            ProductVariant variant = new ProductVariant();
            variant.setProductColor(productColor);
            variant.setProductSize(variantRequest.getProductSize());
            variant.setStockQuantity(variantRequest.getStockQuantity());

            newVariants.add(variant);
        }

        productColor.getVariants().addAll(newVariants);
    }

    /**
     * Cập nhật danh sách ProductColor và ProductVariant
     */
    private void updateProductColors(Set<UpdateProductColorRequest> colorRequests, Products product) {
        Set<ProductColor> existingColors = product.getProductColors();
        if (existingColors == null) {
            existingColors = new HashSet<>();
        }
        Set<ProductColor> updatedColors = new HashSet<>();

        for (UpdateProductColorRequest colorRequest : colorRequests) {
            ProductColor color = null;

            // Nếu ID không null, tìm ProductColor  hiện có
            if (colorRequest.getId() != null) {
                color = existingColors.stream()
                        .filter(existingColor -> existingColor.getId().equals(colorRequest.getId()))
                        .findFirst()
                        .orElse(null);
            }

            // Nếu không tìm thấy, tạo mới
            if (color == null) {
                color = new ProductColor();
                color.setProduct(product);
            }

            // Cập nhật dữ liệu nếu có thay đổi
            if (colorRequest.getPrice() != color.getPrice()) {
                color.setPrice(colorRequest.getPrice());
            }
            if (colorRequest.getColor() != null && !colorRequest.getColor().equals(color.getColor())) {
                color.setColor(colorRequest.getColor());
            }
            if (colorRequest.getImageUrl() != null && !colorRequest.getImageUrl().equals(color.getImageUrl())) {
                fileStorageService.deleteImage(color.getImageUrl());
                color.setImageUrl(colorRequest.getImageUrl());
            }

            // Cập nhật ProductVariant nếu có
            if (colorRequest.getVariants() != null) {
                updateProductVariants(colorRequest.getVariants(), color);
            }

            updatedColors.add(color);
        }

        // ✅ Xóa các ProductColor không có trong request
        existingColors.removeIf(existingColor ->
                updatedColors.stream().noneMatch(updatedColor ->
                        updatedColor.getId() != null && updatedColor.getId().equals(existingColor.getId())));

        // ✅ Thêm hoặc cập nhật ProductColor mới
        existingColors.clear();
        existingColors.addAll(updatedColors);
    }

    private void updateProductVariants(Set<UpdateProductVariantRequest> variantRequests,
                                       ProductColor productColor) {
        Set<ProductVariant> existingVariants = productColor.getVariants();
        Set<ProductVariant> updatedVariants = new HashSet<>();

        // Duyệt qua danh sách request
        for (UpdateProductVariantRequest variantRequest : variantRequests) {
            ProductVariant variant = null;

            // Nếu ID không null, tìm trong danh sách hiện có
            if (variantRequest.getId() != null) {
                variant = existingVariants.stream()
                        .filter(v -> v.getId().equals(variantRequest.getId()))
                        .findFirst()
                        .orElse(null);
            }

            // Nếu không tìm thấy, tạo mới
            if (variant == null) {
                variant = new ProductVariant();
                variant.setProductColor(productColor);
            }

            // Cập nhật dữ liệu
            variant.setProductSize(variantRequest.getProductSize());
            variant.setStockQuantity(variantRequest.getStockQuantity());

            // Thêm vào danh sách cập nhật
            updatedVariants.add(variant);
        }

        // Xóa các phần tử không có trong danh sách request
        existingVariants.removeIf(v ->
                updatedVariants.stream().noneMatch(req -> req.getId() != null && req.getId().equals(v.getId())));

        // Gán danh sách cập nhật
        productColor.getVariants().clear();
        productColor.getVariants().addAll(updatedVariants);
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

            // 🔹 Nếu sắp xếp theo price thì cần xử lý đặc biệt
            Sort sort;
            if ("price".equals(sortBy)) {
                sort = Sort.unsorted(); // Không sử dụng Sort trực tiếp vì cần xử lý trong CriteriaQuery
            } else {
                sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            }

            Pageable pageable = PageRequest.of(Math.max(request.getPage() - 1, 0), request.getSize(), sort);
            Page<Products> productPage = productRepository.findAll(spec, pageable);

            List<ProductDto> productDtos = productPage.getContent().stream().map(productMapper::toDto).collect(Collectors.toList());

            PageableObject<ProductDto> response = new PageableObject<>(
                    productDtos, productPage.hasNext(), productPage.getTotalPages(),
                    productPage.getTotalElements(), productPage.getNumber() + 1
            );
            return ApiResponse.success("Fetched products successfully!", response);
        } catch (Exception e) {
            return ApiResponse.failure(e.getMessage());
        }
    }

}