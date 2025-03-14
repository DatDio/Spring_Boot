package com.example.learn_spring_boot.dtos.requests.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest
{
    private long price;

    private String name;
    private String description;

    private String imageUrl;
}
