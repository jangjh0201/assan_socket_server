package org.asan.domain.watch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WatchDTO {

    @JsonProperty("watch_id")
    private Long id;

    @JsonProperty("watch_uuid")
    private String uuid;

    @JsonProperty("watch_battery")
    private Integer battery;

    @JsonProperty("watch_charging")
    private boolean charging;
}
