package org.assansocketserver.domain.patient.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import org.assansocketserver.domain.sector.dto.SectorDTO;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientResponse {

    @JsonProperty("patient_id")
    private Long id;

    @JsonProperty("patient_name")
    private String name;

    @JsonProperty("patient_number")
    private String number;

    @JsonProperty("patient_gender")
    private String Gender;

    @JsonProperty("patient_room_id")
    private Long sectorId;

    @JsonProperty("patient_room_name")
    private String sectorName;

    @JsonProperty("riskgroup_id")
    private Long riskgroupId;

    @JsonProperty("riskgroup_name")
    private String riskgroupName;

    @JsonProperty("heart_rate_min")
    private int heartRateMin;

    @JsonProperty("heart_rate_max")
    private int heartRateMax;

    @JsonProperty("no_contact")
    private List<PatientDTO> noContact;

    @JsonProperty("restricted_area")
    private List<SectorDTO> restrictedArea;

    @JsonProperty("watch_id")
    private Long watchId;

}
