package org.asansocketserver.domain.image.entity;

import jakarta.persistence.*;
import lombok.*;
import org.asansocketserver.domain.watch.entity.WatchCoordinateProhibition;
import org.asansocketserver.domain.image.enums.SectorType;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "coordinate")
@Entity
public class Coordinate {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="image")
    private Image image;

    private String position;
    private String latitude;
    private String longitude;

    @Column(precision = 10, scale = 4)
    private BigDecimal startX;

    @Column(precision = 10, scale = 4)
    private BigDecimal startY;

    @Column(precision = 10, scale = 4)
    private BigDecimal endX;

    @Column(precision = 10, scale = 4)
    private BigDecimal endY;

    @Enumerated(EnumType.STRING)
    private SectorType setting;

    private Boolean isWeb;

    // OneToMany relationship with cascade type ALL
    @OneToMany(mappedBy = "coordinate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchCoordinateProhibition> watchCoordinateProhibitions;

    public void updateSetting(SectorType setting) {
        this.setting = setting;
    }
}
