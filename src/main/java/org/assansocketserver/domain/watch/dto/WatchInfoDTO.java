package org.assansocketserver.domain.watch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WatchInfoDTO {
    @JsonProperty("watch_status")
    Integer watchStatus;

    @JsonProperty("watch_battery")
    Integer watchBattery;

    @JsonProperty("watch_charging")
    Boolean watchCharging;
}
