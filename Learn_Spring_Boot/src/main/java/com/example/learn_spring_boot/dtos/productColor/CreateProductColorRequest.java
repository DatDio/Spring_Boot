package com.example.learn_spring_boot.dtos.productColor;

import com.example.learn_spring_boot.dtos.productvariant.CreateProductVariantRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductColorRequest {
    private Long productId;   // ID của sản phẩm liên kết
    private BigDecimal price; // Giá của màu sản phẩm
    private MultipartFile image;
    private String color;
    private List<CreateProductVariantRequest> variants;
}
