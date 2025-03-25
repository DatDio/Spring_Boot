package com.example.learn_spring_boot.controllers.client;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.category.CategoryDto;
import com.example.learn_spring_boot.repositories.category.CategoryRepository;
import com.example.learn_spring_boot.services.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/getAll")
    public ApiResponse<List<CategoryDto>> getAllCategory() {

        return categoryService.getAll();
    }
}
