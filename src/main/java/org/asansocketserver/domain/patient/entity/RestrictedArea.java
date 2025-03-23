package org.asansocketserver.domain.patient.entity;

import org.asansocketserver.domain.sector.entity.Sector;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "restricted_area")
@Entity
public class RestrictedArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    // 생성 메서드
    public static RestrictedArea create(Patient patient, Sector sector) {
        return RestrictedArea.builder()
                .patient(patient)
                .sector(sector)
                .build();
    }
}
