package org.assansocketserver.auth.repository;

import org.assansocketserver.auth.entity.Account;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

public class AccountSpecification {

    // 계정의 이름에 keyword가 포함되는 조건
    public static Specification<Account> nameContains(final String keyword) {
        return (Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + keyword.toLowerCase() + "%");

        };
    }

    // wardName이 null이 아니고 빈 문자열이 아닌 조건
    public static Specification<Account> hasWardName() {
        return (Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate notNull = criteriaBuilder.isNotNull(root.get("wardName"));
            Predicate notEmpty = criteriaBuilder.notEqual(root.get("wardName"), "");
            return criteriaBuilder.and(notNull, notEmpty);

        };
    }
}