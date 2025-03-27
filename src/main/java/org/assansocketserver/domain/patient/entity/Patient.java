package org.assansocketserver.domain.patient.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assansocketserver.domain.patient.enums.Gender;
import org.assansocketserver.domain.riskgroup.entity.RiskGroup;
import org.assansocketserver.domain.sector.entity.Sector;
import org.assansocketserver.domain.ward.entity.Ward;
import org.assansocketserver.domain.watch.entity.Watch;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "patient_number", nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "min_heart_rate", nullable = false)
    private Integer minHeartRate;

    @Column(name = "max_heart_rate", nullable = false)
    private Integer maxHeartRate;

    @OneToOne
    @JoinColumn(name = "watch_id")
    private Watch watch;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private Sector sector;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HighRiskGroup> highRiskGroups = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RestrictedArea> restrictedAreas = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NoContact> noContacts = new ArrayList<>();

    public void addRestrictedArea(Sector sector) {
        this.restrictedAreas.add(RestrictedArea.of(this, sector));
    }

    public void addNoContact(Patient patient) {
        this.noContacts.add(NoContact.of(this, patient));
    }

    public void addHighRiskGroup(RiskGroup riskGroup) {
        this.highRiskGroups.add(HighRiskGroup.of(this, riskGroup));
    }

    public void removeRestrictedAreas() {
        this.restrictedAreas.clear();
    }

    public void removeNoContacts() {
        this.noContacts.clear();
    }

    public void removeHighRiskGroups() {
        this.highRiskGroups.clear();
    }

    public void update(Patient patient) {
        Optional.ofNullable(patient.getName()).ifPresent(name -> this.name = name);
        Optional.ofNullable(patient.getNumber()).ifPresent(number -> this.number = number);
        Optional.ofNullable(patient.getGender()).ifPresent(gender -> this.gender = gender);
        Optional.ofNullable(patient.getMinHeartRate()).ifPresent(minHeartRate -> this.minHeartRate = minHeartRate);
        Optional.ofNullable(patient.getMaxHeartRate()).ifPresent(maxHeartRate -> this.maxHeartRate = maxHeartRate);
        Optional.ofNullable(patient.getWatch()).ifPresent(watch -> this.watch = watch);
        Optional.ofNullable(patient.getSector()).ifPresent(sector -> this.sector = sector);
        Optional.ofNullable(patient.getWard()).ifPresent(ward -> this.ward = ward);
    }

    public Boolean isRiskGroup() {
        return !this.highRiskGroups.isEmpty();
    }
}
