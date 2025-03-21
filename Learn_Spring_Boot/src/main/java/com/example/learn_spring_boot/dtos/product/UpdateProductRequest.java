package com.example.learn_spring_boot.dtos.product;

import com.example.learn_spring_boot.dtos.base.BaseDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest extends BaseDto
{
    private long price;

    private String name;
    private String description;
    private String imageUrl;
    //private MultipartFile image;
}
