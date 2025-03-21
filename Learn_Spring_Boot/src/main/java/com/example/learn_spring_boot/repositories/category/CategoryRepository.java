package com.example.learn_spring_boot.repositories.category;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.entities.Brand;
import com.example.learn_spring_boot.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    boolean existsByName(String name);
    List<Category> findAll();
}
