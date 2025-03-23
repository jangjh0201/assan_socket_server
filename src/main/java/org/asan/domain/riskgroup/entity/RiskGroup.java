package org.asan.domain.riskgroup.entity;

import org.asan.domain.patient.entity.Patient;
import org.asan.domain.ward.entity.Ward;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "risk_group")
public class RiskGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * 1) RiskGroup과 N:1 연관관계 예시
     * - RiskGroup 테이블에 ward_id FK가 있다고 가정
     * - "ward"는 RiskGroup 엔티티에서 선언한 필드명과 일치해야 함
     */
    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    /**
     * 2) Patient와 1:N 연관관계 예시
     * - Patient 테이블에 riskgroup_id FK가 있다고 가정
     * - "mappedBy"는 Patient 엔티티에서 선언한 필드명과 일치해야 함
     */
    @OneToMany(mappedBy = "riskGroup")
    @Builder.Default
    private List<Patient> patients = new ArrayList<>();

}
