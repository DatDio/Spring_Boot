package com.example.learn_spring_boot.services.category;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.category.CategoryDto;
import com.example.learn_spring_boot.dtos.category.CreateCategoryRequest;
import com.example.learn_spring_boot.dtos.category.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {
    ApiResponse<List<CategoryDto>> getAll();           // Lấy danh sách danh mục
    ApiResponse<CategoryDto> getById(Long id);         // Lấy danh mục theo ID
    ApiResponse<CategoryDto> createCategory(CreateCategoryRequest request); // Thêm danh mục
    ApiResponse<CategoryDto> updateCategory(UpdateCategoryRequest request); // Cập nhật danh mục
    ApiResponse<String> deleteCategory(Long id);       // Xóa danh mục
}
