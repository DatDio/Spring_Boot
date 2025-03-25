package com.example.learn_spring_boot.repositories.products;

import com.example.learn_spring_boot.entities.Products;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products,Long>,
        JpaSpecificationExecutor<Products> {
    @EntityGraph(attributePaths = {"productColors", "productColors.variants"})
    Optional<Products> findById(Long id);

//    @Query("SELECT p FROM Products p " +
//            "LEFT JOIN FETCH p.colors c " +
//            "LEFT JOIN FETCH c.variants " +
//            "WHERE p.id = :id")
//    Optional<Products> findByIdWithDetails(@Param("id") Long id);

}
