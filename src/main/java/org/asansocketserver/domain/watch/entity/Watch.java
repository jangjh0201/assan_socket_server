package org.asansocketserver.domain.watch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.asansocketserver.domain.patient.entity.Patient;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "watch")
@Entity
public class Watch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "watch_id")
    private Long id;

    private String uuid;

    private String device;

    private String currentLocation;

    @OneToOne
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
