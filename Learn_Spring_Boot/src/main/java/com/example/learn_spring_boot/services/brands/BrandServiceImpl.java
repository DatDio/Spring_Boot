package com.example.learn_spring_boot.services.brands;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.brand.BrandDto;
import com.example.learn_spring_boot.dtos.brand.CreateBrandRequest;
import com.example.learn_spring_boot.dtos.brand.UpdateBrandRequest;
import com.example.learn_spring_boot.entities.Brand;
import com.example.learn_spring_boot.mapper.BrandMapper;
import com.example.learn_spring_boot.repositories.brands.BrandRepository;
import com.example.learn_spring_boot.services.category.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public ApiResponse<List<BrandDto>> getAll() {
        List<Brand> brands = brandRepository.findAll();
        List<BrandDto> brandDtos = brands.stream().map(brandMapper::toDto).toList();
        return ApiResponse.success("Fetched brands successfully!", brandDtos);
    }

    @Override
    public ApiResponse<BrandDto> getById(Long id) {
        Optional<Brand> brandOpt = brandRepository.findById(id);
        if (brandOpt.isEmpty()) {
            return ApiResponse.failure("Brand not found!");
        }
        return ApiResponse.success("Fetched brand successfully!", brandMapper.toDto(brandOpt.get()));
    }

    @Override
    public ApiResponse<BrandDto> createBrand(CreateBrandRequest request) {
        // Kiểm tra nếu thương hiệu đã tồn tại
        if (brandRepository.existsByName(request.getName())) {
            return ApiResponse.failure("Brand already exists!");
        }

        // Chuyển đổi DTO thành Entity
        Brand brand = brandMapper.toEntity(request);
        brandRepository.save(brand);

        return ApiResponse.success("Created brand successfully!", brandMapper.toDto(brand));
    }

    @Override
    public ApiResponse<BrandDto> updateBrand(UpdateBrandRequest request) {
        Brand brand = brandRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Brand not found!"));

        // Cập nhật dữ liệu từ DTO vào Entity
        brandMapper.updateBrandFromDto(request, brand);

        // Lưu vào DB
        brandRepository.save(brand);

        return ApiResponse.success("Updated brand successfully!", brandMapper.toDto(brand));
    }

    @Override
    public ApiResponse<String> deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            return ApiResponse.failure("Brand not found!");
        }
        brandRepository.deleteById(id);
        return ApiResponse.success("Deleted brand successfully!", "Deleted successfully!");
    }
}