package com.example.learn_spring_boot.services.productVariant;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.productvariant.CreateProductVariantRequest;
import com.example.learn_spring_boot.dtos.productvariant.ProductVariantDto;
import com.example.learn_spring_boot.entities.ProductColor;
import com.example.learn_spring_boot.entities.ProductVariant;
import com.example.learn_spring_boot.mapper.ProductVariantMapper;
import com.example.learn_spring_boot.repositories.ProductVariant.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantMapper productVariantMapper;

    @Autowired
    public ProductVariantServiceImpl(ProductVariantRepository productVariantRepository, ProductVariantMapper productVariantMapper) {
        this.productVariantRepository = productVariantRepository;
        this.productVariantMapper = productVariantMapper;
    }

//    @Override
//    public ApiResponse<ProductVariantDto> createProductVariant(CreateProductVariantRequest request, ProductColor productColor) {
//        ProductVariant productVariant = productVariantMapper.toEntity(request,productColor);
//        productVariant.setProductColor(productColor);
//        productVariant = productVariantRepository.save(productVariant);
//        return ApiResponse.success("Tạo biến thể sản phẩm thành công!", productVariantMapper.toDto(productVariant));
//    }
}
