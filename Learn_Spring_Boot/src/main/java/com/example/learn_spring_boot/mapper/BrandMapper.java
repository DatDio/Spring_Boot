package com.example.learn_spring_boot.mapper;

import com.example.learn_spring_boot.dtos.brand.BrandDto;
import com.example.learn_spring_boot.dtos.brand.CreateBrandRequest;
import com.example.learn_spring_boot.dtos.brand.UpdateBrandRequest;
import com.example.learn_spring_boot.dtos.category.UpdateCategoryRequest;
import com.example.learn_spring_boot.entities.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    // Chuyển từ Entity -> DTO
    public BrandDto toDto(Brand brand) {
        if (brand == null) {
            return null;
        }
        BrandDto dto = new BrandDto();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        return dto;
    }

    // Chuyển từ DTO -> Entity (tạo mới)
    public Brand toEntity(CreateBrandRequest dto) {
        if (dto == null) {
            return null;
        }
        Brand brand = new Brand();
        brand.setName(dto.getName());
        return brand;
    }

    // Cập nhật một phần dữ liệu từ DTO vào Entity
    public void updateBrandFromDto(UpdateBrandRequest dto, Brand brand) {
        if (dto == null || brand == null) {
            return;
        }
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            brand.setName(dto.getName());
        }
    }
}