package org.asansocketserver.domain.position.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@RedisHash(value = "position")
public class PositionState {
    @Id
    private Long id;
    private Long wardId;
    private Long sectorId;
    @Indexed
    private String sectorName;
    private Long startTime;
    private Long endTime;

    public static PositionState createPositionState(Long watchId, Long wardId, String sectorName, Long startTime,
            Long endTime) {
        return PositionState.builder()
                .id(watchId)
                .wardId(wardId)
                .sectorName(sectorName)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
