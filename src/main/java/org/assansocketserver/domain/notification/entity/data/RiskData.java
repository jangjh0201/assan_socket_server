package org.assansocketserver.domain.notification.entity.data;

import org.assansocketserver.domain.risk.enums.Severity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiskData extends NotificationData {
    @JsonProperty("risk_id")
    private Long id;
    @JsonProperty("risk_name")
    private String name;
    @JsonProperty("severity")
    private Severity severity;
    @JsonProperty("patient_id")
    private Long patientId;
    @JsonProperty("patient_name")
    private String patientName;
    @JsonProperty("sector_id")
    private Long sectorId;
    @JsonProperty("sector_name")
    private String sectorName;
}