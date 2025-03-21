package org.asansocketserver.domain.watch.entity;

import org.asansocketserver.domain.ward.entity.Sector;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "watch_coordinate_prohibition")
@Entity
public class RestrictedArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "watch_id")
    @JsonIgnore
    private Watch watch;

    @ManyToOne
    @JoinColumn(name = "coordinate_id", nullable = false)
    private Sector coordinate;

    // 생성 메서드
    public static RestrictedArea createProhibition(Watch watch, Sector coordinate) {
        return RestrictedArea.builder()
                .watch(watch)
                .coordinate(coordinate)
                .build();
    }
}
