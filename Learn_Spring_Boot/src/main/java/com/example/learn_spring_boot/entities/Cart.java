package com.example.learn_spring_boot.entities;

import com.example.learn_spring_boot.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "DioCart")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart  extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
}
