package org.asan.domain.ward.repository;

import org.asan.domain.ward.entity.Ward;
// WardSpecification.java
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

public class WardSpecification {
    public static Specification<Ward> nameContains(String keyword) {
        return (Root<Ward> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + keyword.toLowerCase() + "%");
        };
    }
}
