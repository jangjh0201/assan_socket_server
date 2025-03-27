package org.assansocketserver.domain.patient.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import org.assansocketserver.domain.riskgroup.dto.RiskGroupDTO;
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
    private String gender;

    @JsonProperty("patient_room_id")
    private Long sectorId;

    @JsonProperty("patient_room_name")
    private String sectorName;

    @JsonProperty("heart_rate_min")
    private int heartRateMin;

    @JsonProperty("heart_rate_max")
    private int heartRateMax;

    @JsonProperty("riskgroups")
    private List<RiskGroupDTO> highRiskGroups;

    @JsonProperty("no_contact")
    private List<PatientDTO> noContacts;

    @JsonProperty("restricted_area")
    private List<SectorDTO> restrictedAreas;

    @JsonProperty("watch_id")
    private Long watchId;

}
