package com.example.learn_spring_boot.controllers.admin;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.brand.BrandDto;
import com.example.learn_spring_boot.dtos.category.CategoryDto;
import com.example.learn_spring_boot.services.brands.BrandService;
import com.example.learn_spring_boot.services.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {
    @Autowired
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/getAll")
    public ApiResponse<List<BrandDto>> getAllCategory() {
        return brandService.getAll();
    }
}
