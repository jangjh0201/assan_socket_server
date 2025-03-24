package org.asan.domain.position.entity.entity;

import org.asan.domain.sector.entity.Sector;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "beacon")
public class Beacon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wardId;
    private Long sectorId;
    private String sectorName;

    @Column(columnDefinition = "json")
    private String beaconData;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private Sector sector;

    public void updatePos(Long wardId, String sectorName, String beaconData) {
        this.wardId = wardId;
        this.sectorName = sectorName;
        this.beaconData = beaconData;
    }
}
