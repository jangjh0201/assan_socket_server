package org.asansocketserver.domain.ward.entity;

import jakarta.persistence.*;
import lombok.*;

import org.asansocketserver.domain.ward.enums.SectorType;
import org.asansocketserver.domain.watch.entity.RestrictedArea;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "coordinate")
@Entity
public class Sector {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ward")
    private Ward ward;

    private String position;

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
    @OneToMany(mappedBy = "coordinate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestrictedArea> restrictedAreas;

    public void updateSectorType(SectorType sectorType) {
        this.sectorType = sectorType;
    }
}
