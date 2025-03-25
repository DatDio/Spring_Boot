package com.example.learn_spring_boot.entities;

import com.example.learn_spring_boot.entities.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "DioProducts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Products extends BaseEntity {
//    @Column(nullable = false, unique = false)
//    private long price;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 3000)
    private String description;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    @JsonManagedReference
    private Set<ProductColor> productColors ;
}
