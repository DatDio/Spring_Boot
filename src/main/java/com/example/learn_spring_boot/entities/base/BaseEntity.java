package com.example.learn_spring_boot.entities.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updateAt;


    @PrePersist
    public void prePersist(){
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        createAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }
    @PreUpdate
    public void preUpdate(){
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        updateAt = LocalDateTime.now();
    }
}