package com.example.learn_spring_boot.entities;

import com.example.learn_spring_boot.entities.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DIOPRODUCTVARIANTS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"productSize", "product_color_id"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariant extends BaseEntity {
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_color_id", nullable = false)
    private ProductColor productColor;

    @Column(nullable = false)
    private String productSize;

    @Column(nullable = false)
    private int stockQuantity;
}
