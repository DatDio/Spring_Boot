package com.example.learn_spring_boot.config;

import com.example.learn_spring_boot.SystemContants.RoleConstants;
import com.example.learn_spring_boot.entities.Brand;
import com.example.learn_spring_boot.entities.Category;
import com.example.learn_spring_boot.entities.Role;
import com.example.learn_spring_boot.repositories.brands.BrandRepository;
import com.example.learn_spring_boot.repositories.category.CategoryRepository;
import com.example.learn_spring_boot.repositories.roles.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository,
                                   BrandRepository brandRepository,
                                   CategoryRepository categoryRepository) {
        return args -> {
            // ✅ Kiểm tra và tạo ROLE nếu chưa có
            if (roleRepository.findByName(RoleConstants.ADMIN).isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName(RoleConstants.ADMIN);
                roleRepository.save(adminRole);
            }

            if (roleRepository.findByName(RoleConstants.USER).isEmpty()) {
                Role userRole = new Role();
                userRole.setName(RoleConstants.USER);
                roleRepository.save(userRole);
            }

            if (roleRepository.findByName(RoleConstants.AFFILIATE).isEmpty()) {
                Role affiliateRole = new Role();
                affiliateRole.setName(RoleConstants.AFFILIATE);
                roleRepository.save(affiliateRole);
            }

            // ✅ Seed Brands (Thương hiệu quần áo)

            if (brandRepository.findByName("Việt Tiến").isEmpty()) {
                brandRepository.save(new Brand("Việt Tiến"));
            }
            if (brandRepository.findByName("An Phước").isEmpty()) {
                brandRepository.save(new Brand("An Phước"));
            }
            if (brandRepository.findByName("May 10").isEmpty()) {
                brandRepository.save(new Brand("May 10"));
            }
            if (brandRepository.findByName("Blue Exchange").isEmpty()) {
                brandRepository.save(new Brand("Blue Exchange"));
            }

            // ✅ Seed Categories (Danh mục quần áo)
            if (categoryRepository.findByName("Áo thun").isEmpty()) {
                categoryRepository.save(new Category("Áo thun"));
            }
            if (categoryRepository.findByName("Áo sơ mi").isEmpty()) {
                categoryRepository.save(new Category("Áo sơ mi"));
            }
            if (categoryRepository.findByName("Quần jean").isEmpty()) {
                categoryRepository.save(new Category("Quần jean"));
            }
            if (categoryRepository.findByName("Đầm váy").isEmpty()) {
                categoryRepository.save(new Category("Đầm váy"));
            }
            if (categoryRepository.findByName("Đồ thể thao").isEmpty()) {
                categoryRepository.save(new Category("Đồ thể thao"));
            }
            };
    }
}