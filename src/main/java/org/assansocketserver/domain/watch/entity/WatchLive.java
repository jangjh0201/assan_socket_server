package org.assansocketserver.domain.watch.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@RedisHash(value = "watch", timeToLive = 100)
public class WatchLive {
    @Id
    private Long id;
    @Indexed
    private boolean live;

    public static WatchLive createWatchLive(Long watchId) {
        return WatchLive.builder()
                .id(watchId)
                .live(true)
                .build();
    }
}
