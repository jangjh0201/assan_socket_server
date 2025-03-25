package org.asan.domain.position.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonProperty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@RedisHash(value = "position")
public class PositionState {
    @Id
    private Long id;
    private Long imageId;
    @Indexed
    private String position;
    private Long startTime;
    private Long endTime;

    public static PositionState createPositionState(Long watchId, Long wardId, String sectorName, Long startTime,
            Long endTime) {
        return PositionState.builder()
                .id(watchId)
                .imageId(wardId)
                .position(sectorName)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
