package org.asan.domain.risk.dto;

import org.asan.domain.risk.entity.Risk;
import org.asan.domain.risk.enums.Severity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 자동 제외
public class RiskDTO {

    @JsonProperty("risk_id")
    private Long id;

    @JsonProperty("risk_name")
    private String name;

    @JsonProperty("severity")
    private Severity severity;

    @JsonProperty("availability")
    private Boolean availability;

    @JsonProperty("risk_type_id")
    private Integer riskTypeId;

    public static RiskDTO fromEntity(Risk risk) {
        return RiskDTO.builder()
                .id(risk.getId())
                .name(risk.getRiskType().getName())
                .severity(risk.getSeverity())
                .availability(risk.getAvailability())
                .riskTypeId(risk.getRiskType().getId().intValue())
                .build();
    }
}
