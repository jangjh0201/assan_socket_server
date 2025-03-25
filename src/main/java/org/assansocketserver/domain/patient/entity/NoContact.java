package org.assansocketserver.domain.patient.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "no_contact")
public class NoContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "no_caontact_id", nullable = false)
    private Patient noContactPatient;

    public static NoContact of(Patient patient, Patient noContactPatient) {
        return NoContact.builder()
                .patient(patient)
                .noContactPatient(noContactPatient)
                .build();
    }
}
