package org.assansocketserver.domain.patient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PatientSocketDTO {

    @JsonProperty("patient_id")
    Long id;

    @JsonProperty("patient_name")
    String name;

    @JsonProperty("patient_number")
    String number;

    @JsonProperty("patient_room_name")
    String sectorName;

    @JsonProperty("current_sector_id")
    Long currentLocationId;

    @JsonProperty("watch_status")
    Integer watchStatus;

    @JsonProperty("watch_battery")
    Integer watchBattery;

    @JsonProperty("watch_charging")
    Boolean watchCharging;

    @JsonProperty("riskgroup")
    Boolean riskGroup;

    @JsonProperty("active_status")
    Integer activeStatus;
}
