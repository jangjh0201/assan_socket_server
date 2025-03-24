package org.asan.domain.stat.dto;

import lombok.*;

import java.util.List;

import org.asan.batch.cdc.entity.SensorRow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatDTO {

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("ward_id")
    private Long wardId;

    @JsonProperty("ward_name")
    private String wardName;

    @JsonProperty("current_storage")
    private Double currentStorage;

    @JsonProperty("total_storage")
    private Double totalStorage;

    @JsonProperty("logs_valid")
    private Boolean logsValid;

    @JsonProperty("current_watch")
    private Integer currentWatch;

    @JsonProperty("total_watch")
    private Integer totalWatch;

    @JsonProperty("current_patient")
    private Integer currentPatient;

    @JsonProperty("total_patient")
    private Integer totalPatient;

    @JsonProperty("stats")
    List<SensorRow> sensorRowList;

}
