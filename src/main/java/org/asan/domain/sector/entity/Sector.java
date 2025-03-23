package org.asan.domain.sector.entity;

import java.util.List;
import java.util.ArrayList;

import org.asan.domain.beacon.entity.Beacon;
import org.asan.domain.patient.entity.Patient;
import org.asan.domain.patient.entity.RestrictedArea;
import org.asan.domain.sector.enums.SectorType;
import org.asan.domain.ward.entity.Ward;

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
    @Column(name = "start_x")
    private Double startX;

    @Column(name = "start_y")
    private Double startY;

    // 구역의 끝 좌표
    @Column(name = "end_x")
    private Double endX;

    @Column(name = "end_y")
    private Double endY;

    // 구역 타입(예: "restricted", "public" 등)
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Builder.Default
    private SectorType sectorType = SectorType.PUBLIC;;

    @OneToMany(mappedBy = "sector")
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
