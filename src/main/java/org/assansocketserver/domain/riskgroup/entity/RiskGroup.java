package org.assansocketserver.domain.riskgroup.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import org.assansocketserver.domain.patient.entity.HighRiskGroup;
import org.assansocketserver.domain.ward.entity.Ward;

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

    @OneToMany(mappedBy = "riskGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HighRiskGroup> highRiskGroups = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

}
