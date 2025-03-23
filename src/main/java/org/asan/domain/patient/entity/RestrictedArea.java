package org.asan.domain.patient.entity;

import org.asan.domain.sector.entity.Sector;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "restricted_area")
public class RestrictedArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 필요 시 @ManyToOne으로 Patient, Sector와 매핑 가능
    @ManyToOne
    @JoinColumn(name = "patient_id", updatable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "sector_id", updatable = false)
    private Sector sector;

    public static RestrictedArea of(Patient patient, Sector sector) {
        return RestrictedArea.builder()
                .patient(patient)
                .sector(sector)
                .build();
    }

}
