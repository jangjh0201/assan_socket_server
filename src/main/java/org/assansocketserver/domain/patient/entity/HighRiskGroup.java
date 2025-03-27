package org.assansocketserver.domain.patient.entity;

import org.assansocketserver.domain.riskgroup.entity.RiskGroup;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "high_risk_group")
public class HighRiskGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", updatable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "risk_group_id", updatable = false)
    private RiskGroup riskGroup;

    public static HighRiskGroup of(Patient patient, RiskGroup riskGroup) {
        return HighRiskGroup.builder()
                .patient(patient)
                .riskGroup(riskGroup)
                .build();
    }
}
