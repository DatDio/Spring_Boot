package com.example.learn_spring_boot.dtos.product;

import com.example.learn_spring_boot.dtos.base.BaseDto;
import com.example.learn_spring_boot.dtos.productColor.CreateProductColorRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CreateProductRequest{
    private String name;
    private String description;
    private String imageUrl;
    private Long brandId;
    private Long categoryId;
    private Set<CreateProductColorRequest> productColors;
}
