package org.asansocketserver.domain.watch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.asansocketserver.domain.image.entity.Coordinate;
import org.asansocketserver.domain.watch.dto.request.WatchUpdateRequestDto;
import org.asansocketserver.domain.watch.enums.Gender;
import org.asansocketserver.domain.watch.enums.HighRisk;

import java.util.ArrayList;
import java.util.List;

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

    private String name;

    private String host;

    private int minHR;

    private int maxHR;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private HighRisk highRisk;

    private String currentLocation;

    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchCoordinateProhibition> prohibitedCoordinateList = new ArrayList<>();

    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchNoContact> noContactWatchList = new ArrayList<>();

    public void updateCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;

    }

    public void addProhibitedCoordinate(Coordinate coordinate) {
        WatchCoordinateProhibition restriction = WatchCoordinateProhibition.createProhibition(this, coordinate);
        prohibitedCoordinateList.add(restriction);
    }

    public void addNoContactWatch(Watch noContactWatch) {
        WatchNoContact watchNoContact = WatchNoContact.createNoContact(this, noContactWatch);
        noContactWatchList.add(watchNoContact);
    }

    public static Watch createWatch(String uuid, String device) {
        return Watch.builder()
                .uuid(uuid)
                .device(device)
                .name("지정되지않음")
                .host("지정되지않음")
                .minHR(60)
                .maxHR(130)
                .gender(Gender.MALE)
                .highRisk(HighRisk.없음)
                .build();
    }

    public void updateWatch(WatchUpdateRequestDto requestDto) {
        this.name = requestDto.name();
        this.host = requestDto.host();
    }

    public void updateWatchForTransfer(Watch watch) {
        this.name = watch.getName();
        this.host = watch.getHost();
        this.gender = watch.getGender();
        this.highRisk = watch.getHighRisk();
        this.minHR = watch.getMinHR();
        this.maxHR = watch.getMaxHR();

        // 기존 리스트를 지우고 sendWatch의 정보를 추가
        this.prohibitedCoordinateList.clear();
        for (WatchCoordinateProhibition prohibition : watch.getProhibitedCoordinateList()) {
            this.addProhibitedCoordinate(prohibition.getCoordinate());
        }

        for (WatchNoContact noContact : watch.getNoContactWatchList()) {
            this.addNoContactWatch(noContact.getNoContactWatch());
        }
    }

    public void updateWatchNameForTransfer(String watchName) {
        this.name = watchName;
    }

}
