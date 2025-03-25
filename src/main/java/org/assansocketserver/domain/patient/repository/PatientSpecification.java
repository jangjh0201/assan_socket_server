package org.assansocketserver.domain.patient.repository;

import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.patient.enums.Gender;
import org.assansocketserver.domain.ward.entity.Ward;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

public class PatientSpecification {

    // 해당 병동에 속한 환자 조건
    public static Specification<Patient> wardIs(final Ward ward) {
        return (Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("ward"), ward);

        };
    }

    // 성별 조건 (gender 값이 비어있으면 전체 조회)
    public static Specification<Patient> genderIs(final String genderStr) {
        return (Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (genderStr == null || genderStr.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Gender gender = Gender.valueOf(genderStr);
            return criteriaBuilder.equal(root.get("gender"), gender);
        };
    }

    // 이름에 키워드가 포함된 환자 조건
    public static Specification<Patient> searchByKeyword(final String keyword) {
        return (Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likeKeyword = "%" + keyword.toLowerCase() + "%";

            // 이름 검색
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likeKeyword);
            // 번호 검색
            Predicate numberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("number")), likeKeyword);

            // sector 검색 (연관 엔티티 조인)
            Join<Object, Object> sectorJoin = root.join("sector", JoinType.LEFT);
            Predicate sectorPredicate = criteriaBuilder.like(criteriaBuilder.lower(sectorJoin.get("name")),
                    likeKeyword);

            // watch 검색 (연관 엔티티 조인)
            Join<Object, Object> watchJoin = root.join("watch", JoinType.LEFT);
            // watch의 id가 숫자형인 경우 문자열 변환 후 검색하거나,
            // watch의 다른 텍스트 속성(예: serialNumber 등)이 있다면 그 속성으로 검색할 수 있습니다.
            Predicate watchPredicate = criteriaBuilder.like(criteriaBuilder.lower(watchJoin.get("id").as(String.class)),
                    likeKeyword);

            // OR 조건으로 모든 조건을 결합
            return criteriaBuilder.or(namePredicate, numberPredicate, sectorPredicate, watchPredicate);
        };
    }

}