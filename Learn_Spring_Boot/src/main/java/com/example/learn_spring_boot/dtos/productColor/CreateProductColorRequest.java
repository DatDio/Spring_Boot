package com.example.learn_spring_boot.dtos.productColor;

import com.example.learn_spring_boot.dtos.productvariant.CreateProductVariantRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductColorRequest {
    //private Long productId;   // ID của sản phẩm liên kết
    private double price; // Giá của màu sản phẩm
    private String imageUrl;
    private String color;
    private Set<CreateProductVariantRequest> variants;
}
