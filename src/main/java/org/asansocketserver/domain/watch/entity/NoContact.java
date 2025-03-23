package org.asansocketserver.domain.watch.entity;

import org.asansocketserver.domain.patient.entity.Patient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "watch_no_contact")
@Entity
public class NoContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "no_contact_id")
    private Patient noContact;

    public static NoContact createNoContact(Patient watch, Patient noContact) {
        return NoContact.builder()
                .patient(watch)
                .noContact(noContact)
                .build();
    }
}
