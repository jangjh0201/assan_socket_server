package org.asansocketserver.domain.sector.entity;

import jakarta.persistence.*;
import lombok.*;

import org.asansocketserver.domain.patient.entity.RestrictedArea;
import org.asansocketserver.domain.sector.enums.SectorType;
import org.asansocketserver.domain.ward.entity.Ward;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "sector")
@Entity
public class Sector {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "ward")
    private Ward ward;

    @Column(precision = 10, scale = 4)
    private BigDecimal startX;

    @Column(precision = 10, scale = 4)
    private BigDecimal startY;

    @Column(precision = 10, scale = 4)
    private BigDecimal endX;

    @Column(precision = 10, scale = 4)
    private BigDecimal endY;

    @Enumerated(EnumType.STRING)
    private SectorType sectorType;

    // OneToMany relationship with cascade type ALL
    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestrictedArea> restrictedAreas;

    public void updateSectorType(SectorType sectorType) {
        this.sectorType = sectorType;
    }
}
