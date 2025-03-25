package org.assansocketserver.domain.patient.dto;

import org.assansocketserver.domain.patient.enums.Gender;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientDTO {

    @JsonProperty("patient_id")
    private Long id;

    @JsonProperty("patient_name")
    private String name;

    @JsonProperty("patient_number")
    private String number;

    @JsonProperty("patient_gender")
    private Gender gender;

    @JsonProperty("patient_room_id")
    private Long sectorId;

    @JsonProperty("patient_room_name")
    private String sectorName;

    @JsonProperty("watch_id")
    private Long watchId;

    @JsonProperty("watch_status")
    private Integer watchStatus;

    @JsonProperty("watch_battery")
    private Integer watchBattery;

    @JsonProperty("watch_charging")
    private Boolean watchCharging;
}
