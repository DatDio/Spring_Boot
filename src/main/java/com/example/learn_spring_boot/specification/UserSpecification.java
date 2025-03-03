package com.example.learn_spring_boot.specification;

import com.example.learn_spring_boot.dtos.requests.users.SearchUserRequest;
import com.example.learn_spring_boot.entities.Users;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class UserSpecification {
    public static Specification<Users> filterUsers(SearchUserRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(request.getId() != null){
                predicates.add(criteriaBuilder.equal(root.get("id"), request.getId()));
            }
            if (StringUtils.hasText(request.getUserName())) {
                predicates.add(criteriaBuilder.like(root.get("userName"), "%" + request.getUserName() + "%"));
            }
            if (StringUtils.hasText(request.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + request.getEmail() + "%"));
            }
            if (StringUtils.hasText(request.getPhoneNumber())) {
                predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%" + request.getPhoneNumber() + "%"));
            }
//            if (request.getCreatedFrom() != null) {
//                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created_At"), request.getCreatedFrom()));
//            }
//            if (request.getCreatedTo() != null) {
//                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("created_At"), request.getCreatedTo()));
//            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

