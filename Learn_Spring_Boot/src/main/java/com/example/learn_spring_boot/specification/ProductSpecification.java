package com.example.learn_spring_boot.specification;
import com.example.learn_spring_boot.entities.ProductColor;
import com.example.learn_spring_boot.entities.Products;
import com.example.learn_spring_boot.dtos.product.SearchProductRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import org.springframework.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Products> filterProducts(SearchProductRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), request.getId()));
            }
            if (StringUtils.hasText(request.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getName() + "%"));
            }
            if (StringUtils.hasText(request.getDescription())) {
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + request.getDescription() + "%"));
            }

            // 🔹 Lọc theo Brand
            if (request.getBrandId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("brand").get("id"), request.getBrandId()));
            }

            // 🔹 Lọc theo Category
            if (request.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), request.getCategoryId()));
            }

            // 🔹 LEFT JOIN với ProductColor để lọc theo price
            Join<Products, ProductColor> productColorJoin = root.join("productColors", JoinType.LEFT);

            if (request.getPriceFrom() > 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(productColorJoin.get("price"), request.getPriceFrom()));
            }
            if (request.getPriceTo() > 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(productColorJoin.get("price"), request.getPriceTo()));
            }

            if (request.getCreatedFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createAt"), request.getCreatedFrom()));
            }
            if (request.getCreatedTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createAt"), request.getCreatedTo()));
            }

            // 🔹 Sắp xếp theo price nếu cần
            if ("price".equals(request.getSortBy())) {
                Order order = request.getDirection().equalsIgnoreCase("desc")
                        ? criteriaBuilder.desc(productColorJoin.get("price"))
                        : criteriaBuilder.asc(productColorJoin.get("price"));
                query.orderBy(order);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }



}

