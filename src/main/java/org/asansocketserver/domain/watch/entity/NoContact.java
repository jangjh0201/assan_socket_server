package org.asansocketserver.domain.watch.entity;

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
    @JoinColumn(name = "watch_id")
    @JsonIgnore
    private Watch watch;

    @ManyToOne
    @JoinColumn(name = "no_contact_watch_id")
    private Watch noContact;

    public static NoContact createNoContact(Watch watch, Watch noContact) {
        return NoContact.builder()
                .watch(watch)
                .noContact(noContact)
                .build();
    }
}
