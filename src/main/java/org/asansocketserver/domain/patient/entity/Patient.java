package org.asansocketserver.domain.patient.entity;

import java.util.ArrayList;
import java.util.List;

import org.asansocketserver.domain.watch.entity.Watch;
import org.asansocketserver.domain.watch.enums.Gender;
import org.asansocketserver.domain.watch.enums.RiskType;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "patient")
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    private String name;

    private Integer minHeartRate;

    private Integer maxHeartRate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private RiskType riskType;

    private String ward;

    @OneToOne
    @JoinColumn(name = "watch_id")
    private Watch watch;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RestrictedArea> restrictedAreas = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NoContact> noContacts = new ArrayList<>();

}
