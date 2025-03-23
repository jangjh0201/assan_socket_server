package org.asan.domain.beacon.entity;

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

    @Column(name = "bssid")
    private String bssid;

    @Column(name = "rssi")
    private String rssi;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private Sector sector;
}
