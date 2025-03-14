package com.example.learn_spring_boot.repositories.products;

import com.example.learn_spring_boot.entities.Products;
import com.example.learn_spring_boot.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Products,Long>,
        JpaSpecificationExecutor<Products> {

}
