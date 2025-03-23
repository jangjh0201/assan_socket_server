package org.asan.domain.risk.entity;

import org.asan.domain.risk.enums.Severity;
import org.asan.domain.ward.entity.Ward;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "risk")
public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 가용성 (예: 'true', 'false' 등)
    @Column(name = "availability", nullable = false)
    private Boolean availability;

    // 위험 심각도 (예: 'low', 'Mid', 'high' 등)
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity;

    // Ward와 다대일(ManyToOne) 관계
    // 실제로 Ward 엔티티에 @OneToMany(mappedBy="ward")가 선언되어 있어야 함
    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    // RiskType과 다대일(ManyToOne) 관계
    // 실제로 RiskType 엔티티에 @OneToMany(mappedBy="riskType")가 선언되어 있어야 함
    @ManyToOne
    @JoinColumn(name = "risk_type_id")
    private RiskType riskType;

    // Severity 변경
    public void changeSeverity(Severity severity) {
        this.severity = severity;
    }

    // 사용 가능 여부 변경
    public void changeAvailability(boolean availability) {
        this.availability = availability;
    }
}
