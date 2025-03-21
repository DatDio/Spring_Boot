package com.example.learn_spring_boot.services.productColor;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.productColor.CreateProductColorRequest;
import com.example.learn_spring_boot.dtos.productColor.ProductColorDto;
import com.example.learn_spring_boot.entities.ProductColor;
import com.example.learn_spring_boot.entities.Products;
import com.example.learn_spring_boot.mapper.ProductColorMapper;
import com.example.learn_spring_boot.repositories.productColor.ProductColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductColorServiceImpl implements ProductColorService {
    private final ProductColorRepository productColorRepository;
    private final ProductColorMapper productColorMapper;

    @Autowired
    public ProductColorServiceImpl(ProductColorRepository productColorRepository, ProductColorMapper productColorMapper) {
        this.productColorRepository = productColorRepository;
        this.productColorMapper = productColorMapper;
    }

    @Override
    public ApiResponse<ProductColorDto> createProductColor(CreateProductColorRequest request, Products product) {
        ProductColor productColor = productColorMapper.toEntity(request);
        productColor.setProduct(product);
        productColor = productColorRepository.save(productColor);
        return ApiResponse.success("Tạo màu sản phẩm thành công!", productColorMapper.toDto(productColor));
    }

    @Override
    public ApiResponse<List<ProductColorDto>> getByProductId(Long productId) {
        List<ProductColor> colors = productColorRepository.findByProductId(productId);
        List<ProductColorDto> colorDtos = colors.stream().map(productColorMapper::toDto).collect(Collectors.toList());
        return ApiResponse.success("Lấy danh sách màu sản phẩm thành công!", colorDtos);
    }
}
