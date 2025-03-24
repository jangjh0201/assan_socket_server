package org.asan.domain.watch.entity;

import org.asan.domain.patient.entity.Patient;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "watch")
public class Watch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String device;

    private String currentLocation;

    @OneToOne(mappedBy = "watch")
    private Patient patient;

    public void updateCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;

    }

    public static Watch createWatch(String uuid, String device) {
        return Watch.builder()
                .uuid(uuid)
                .device(device)
                .patient(null)
                .build();
    }

}
