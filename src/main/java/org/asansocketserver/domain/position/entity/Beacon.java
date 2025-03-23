package org.asansocketserver.domain.position.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Beacon {
    @Id
    @GeneratedValue
    private int id;
    private Long wardId;
    private Long sectorId;
    private String sectorName;

    @Column(columnDefinition = "json")
    private String beaconData;
}
