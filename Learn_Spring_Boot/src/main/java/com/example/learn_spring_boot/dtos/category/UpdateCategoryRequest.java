package com.example.learn_spring_boot.dtos.category;

import com.example.learn_spring_boot.dtos.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryRequest extends BaseDto {
    private String name;
}
