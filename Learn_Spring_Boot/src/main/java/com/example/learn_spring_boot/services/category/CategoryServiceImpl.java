package com.example.learn_spring_boot.services.category;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.category.CategoryDto;
import com.example.learn_spring_boot.dtos.category.CreateCategoryRequest;
import com.example.learn_spring_boot.dtos.category.UpdateCategoryRequest;
import com.example.learn_spring_boot.entities.Category;
import com.example.learn_spring_boot.mapper.CategoryMapper;
import com.example.learn_spring_boot.repositories.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    // 🟢 Lấy tất cả danh mục
    @Override
    public ApiResponse<List<CategoryDto>> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = categories.stream().map(categoryMapper::toDto).toList();
        return ApiResponse.success("Fetched categories successfully!", categoryDtos);
    }

    // 🟢 Lấy danh mục theo ID
    @Override
    public ApiResponse<CategoryDto> getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElse(null);

        if (category == null) {
            return ApiResponse.failure("Danh mục không tồn tại!");
        }

        return ApiResponse.success("Lấy danh mục thành công!", categoryMapper.toDto(category));
    }

    // 🟢 Tạo danh mục mới
    @Override
    public ApiResponse<CategoryDto> createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            return ApiResponse.failure("Danh mục đã tồn tại!");
        }

        Category category = categoryMapper.toEntity(request);
        categoryRepository.save(category);

        return ApiResponse.success("Thêm danh mục thành công!", categoryMapper.toDto(category));
    }

    // 🟢 Cập nhật danh mục
    @Override
    public ApiResponse<CategoryDto> updateCategory(UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(request.getId())
                .orElse(null);

        if (category == null) {
            return ApiResponse.failure("Danh mục không tồn tại!");
        }

        // Cập nhật dữ liệu từ DTO vào Entity
        categoryMapper.updateCategoryFromDto(request, category);

        // Lưu vào DB
        categoryRepository.save(category);

        // Trả về CategoryDto
        return ApiResponse.success("Cập nhật danh mục thành công!", categoryMapper.toDto(category));
    }

    // 🟢 Xóa danh mục
    @Override
    public ApiResponse<String> deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElse(null);

        if (category == null) {
            return ApiResponse.failure("Danh mục không tồn tại!");
        }

        categoryRepository.delete(category);
        return ApiResponse.success("Xóa danh mục thành công!", "Deleted");
    }
}