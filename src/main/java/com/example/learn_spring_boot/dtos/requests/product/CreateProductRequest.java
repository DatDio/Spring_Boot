package com.example.learn_spring_boot.dtos.requests.product;

import com.example.learn_spring_boot.dtos.requests.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CreateProductRequest extends BaseDto {
    private long price;

    private String name;
    private String description;

    private MultipartFile image;
}
