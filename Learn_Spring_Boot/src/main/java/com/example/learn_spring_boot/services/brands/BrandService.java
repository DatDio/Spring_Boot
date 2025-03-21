package com.example.learn_spring_boot.services.brands;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.brand.BrandDto;
import com.example.learn_spring_boot.dtos.brand.CreateBrandRequest;
import com.example.learn_spring_boot.dtos.brand.UpdateBrandRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandService {
    ApiResponse<List<BrandDto>> getAll();             // Lấy danh sách tất cả thương hiệu
    ApiResponse<BrandDto> getById(Long id);           // Lấy thương hiệu theo ID
    ApiResponse<BrandDto> createBrand(CreateBrandRequest request); // Thêm mới thương hiệu
    ApiResponse<BrandDto> updateBrand(UpdateBrandRequest request); // Cập nhật thương hiệu
    ApiResponse<String> deleteBrand(Long id);         // Xóa thương hiệu
}
