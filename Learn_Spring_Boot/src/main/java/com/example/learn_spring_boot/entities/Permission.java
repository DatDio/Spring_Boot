package com.example.learn_spring_boot.entities;

import com.example.learn_spring_boot.entities.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name ="DioPermission")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends BaseEntity {
    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
}
