package com.example.learn_spring_boot.mapper;

import com.example.learn_spring_boot.dtos.category.CategoryDto;
import com.example.learn_spring_boot.dtos.category.CreateCategoryRequest;
import com.example.learn_spring_boot.dtos.category.UpdateCategoryRequest;
import com.example.learn_spring_boot.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    // Chuyển từ Entity -> DTO
    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    // Chuyển từ DTO -> Entity (tạo mới)
    public Category toEntity(CreateCategoryRequest dto) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    // Cập nhật một phần dữ liệu (update Category từ DTO)
    public void updateCategoryFromDto(UpdateCategoryRequest dto, Category category) {
        if (dto == null || category == null) {
            return;
        }
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            category.setName(dto.getName());
        }
    }
}
