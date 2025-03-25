package org.assansocketserver.domain.ward.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import org.assansocketserver.auth.entity.Account;
import org.assansocketserver.domain.risk.entity.Risk;
import org.assansocketserver.domain.riskgroup.entity.RiskGroup;
import org.assansocketserver.domain.sector.entity.Sector;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ward")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "image")
    private String image;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    // RiskGroup과 다대일 관계 (RiskGroup 쪽에 @OneToMany(mappedBy="riskGroup")가 있어야 함)
    @OneToMany(mappedBy = "ward", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RiskGroup> riskGroup = new ArrayList<>();

    // RiskConfig와 1:N (Risk 엔티티에서 ward를 참조)
    @OneToMany(mappedBy = "ward", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Risk> risks = new ArrayList<>();

    @OneToMany(mappedBy = "ward", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Sector> sectors = new ArrayList<>();

}
