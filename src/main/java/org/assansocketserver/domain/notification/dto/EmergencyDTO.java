package org.assansocketserver.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmergencyDTO {
    @JsonProperty("emergency_id")
    private Long id;

    @JsonProperty("emergency_name")
    private String name;

    @JsonProperty("patient_id")
    private Long patientId;

    @JsonProperty("patient_name")
    private String patientName;

    @JsonProperty("patient_number")
    private String patientNumber;

    @JsonProperty("patient_room_name")
    private String patientRoomName;

    @JsonProperty("current_sector_name")
    private String currentSectorName;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("timestamp")
    private String timestamp;

}
