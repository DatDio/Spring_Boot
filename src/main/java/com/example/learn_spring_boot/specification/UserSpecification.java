package com.example.learn_spring_boot.specification;

import com.example.learn_spring_boot.entities.Users;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class UserSpecification {
    public static Specification<Users> filterUsers(
            String userName, String email, String phoneNumber,
            LocalDate createdFrom, LocalDate createdTo) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(userName)) {
                predicates.add(criteriaBuilder.like(root.get("userName"), "%" + userName + "%"));
            }
            if (StringUtils.hasText(email)) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }
            if (StringUtils.hasText(phoneNumber)) {
                predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%" + phoneNumber + "%"));
            }
            if (createdFrom != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdFrom));
            }
            if (createdTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdTo));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

