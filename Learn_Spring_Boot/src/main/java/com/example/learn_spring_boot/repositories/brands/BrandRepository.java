package com.example.learn_spring_boot.repositories.brands;

import com.example.learn_spring_boot.entities.Brand;
import com.example.learn_spring_boot.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);
    List<Brand> findAll();

    boolean existsByName(String name);
}
