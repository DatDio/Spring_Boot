package com.example.learn_spring_boot.repositories.productColor;

import com.example.learn_spring_boot.entities.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductColorRepository  extends JpaRepository<ProductColor, Long> {
    List<ProductColor> findByProductId(Long productId);
}
