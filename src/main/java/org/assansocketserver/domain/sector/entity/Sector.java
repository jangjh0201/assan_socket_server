package org.assansocketserver.domain.sector.entity;

import java.util.List;

import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.patient.entity.RestrictedArea;
import org.assansocketserver.domain.position.entity.entity.Beacon;
import org.assansocketserver.domain.sector.enums.SectorType;
import org.assansocketserver.domain.ward.entity.Ward;

import java.math.BigDecimal;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sector")
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // 구역의 시작 좌표
    @Column(name = "start_x", precision = 10, scale = 4)
    private BigDecimal startX;

    @Column(name = "start_y")
    private BigDecimal startY;

    // 구역의 끝 좌표
    @Column(name = "end_x")
    private BigDecimal endX;

    @Column(name = "end_y")
    private BigDecimal endY;

    // 구역 타입(예: "restricted", "public" 등)
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Builder.Default
    private SectorType sectorType = SectorType.PUBLIC;;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Beacon> beacons = new ArrayList<>();

    @OneToMany(mappedBy = "sector")
    @Builder.Default
    private List<Patient> patients = new ArrayList<>();

    // RestrictedArea와 1:N (한 구역(Sector)에 여러 RestrictedArea가 생길 수 있음)
    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RestrictedArea> restrictedAreas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    // SectorType 변경
    public void changeSectorType(SectorType sectorType) {
        this.sectorType = sectorType;
    }
}
