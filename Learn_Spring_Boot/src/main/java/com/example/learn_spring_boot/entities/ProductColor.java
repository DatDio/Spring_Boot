package com.example.learn_spring_boot.entities;

import com.example.learn_spring_boot.entities.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "DioProductColors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductColor extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;

    @Column(nullable = false)
    private BigDecimal price;

    private String color;
    private String imageUrl;

    @JsonManagedReference
    @OneToMany(mappedBy = "productColor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductVariant> variants;

}
