package com.example.learn_spring_boot.dtos.product;

import com.example.learn_spring_boot.dtos.base.BaseDto;
import com.example.learn_spring_boot.dtos.productColor.CreateProductColorRequest;
import com.example.learn_spring_boot.dtos.productColor.UpdateProductColorRequest;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest extends BaseDto
{
    private String name;
    private String description;
    private String imageUrl;
    private Long brandId;
    private Long categoryId;
    private Set<UpdateProductColorRequest> productColors;
}
