package com.example.learn_spring_boot.config;

import com.example.learn_spring_boot.SystemContants.RoleConstants;
import com.example.learn_spring_boot.entities.*;
import com.example.learn_spring_boot.repositories.brands.BrandRepository;
import com.example.learn_spring_boot.repositories.category.CategoryRepository;
import com.example.learn_spring_boot.repositories.roles.RoleRepository;
import com.example.learn_spring_boot.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final RoleRepository roleRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            seedRoles();
            seedBrands();
            seedCategories();
            seedAdminUser();
        };
    }

    private void seedRoles() {
        if (roleRepository.findByName(RoleConstants.ADMIN).isEmpty()) {
            roleRepository.save(new Role(RoleConstants.ADMIN));
        }
        if (roleRepository.findByName(RoleConstants.USER).isEmpty()) {
            roleRepository.save(new Role(RoleConstants.USER));
        }
        if (roleRepository.findByName(RoleConstants.AFFILIATE).isEmpty()) {
            roleRepository.save(new Role(RoleConstants.AFFILIATE));
        }
    }

    private void seedBrands() {
        String[] brands = {"Việt Tiến", "An Phước", "May 10", "Blue Exchange"};
        for (String brand : brands) {
            if (brandRepository.findByName(brand).isEmpty()) {
                brandRepository.save(new Brand(brand));
            }
        }
    }

    private void seedCategories() {
        String[] categories = {"Áo thun", "Áo sơ mi", "Quần jean", "Đầm váy", "Đồ thể thao"};
        for (String category : categories) {
            if (categoryRepository.findByName(category).isEmpty()) {
                categoryRepository.save(new Category(category));
            }
        }
    }

    private void seedAdminUser() {
        if (userRepository.findByEmail("admin@dio.com").isEmpty()) {
            Role adminRole = roleRepository.findByName(RoleConstants.ADMIN).orElseThrow(
                    () -> new RuntimeException("Role ADMIN chưa được tạo!")
            );

            Users admin = Users.builder()
                    .userName("admin")
                    .email("admin@dio.com")
                    .passWord(passwordEncoder.encode("admin123")) // Mật khẩu mã hóa
                    .roles(Set.of(adminRole)) // Gán quyền ADMIN
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Tài khoản Admin đã được tạo: admin@dio.com / admin123");
        } else {
            System.out.println("⚠️ Tài khoản Admin đã tồn tại.");
        }
    }
}
