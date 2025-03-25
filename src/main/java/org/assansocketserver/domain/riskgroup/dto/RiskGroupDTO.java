package org.assansocketserver.domain.riskgroup.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskGroupDTO {

    @JsonProperty("riskgroup_id")
    private Long id;

    @JsonProperty("riskgroup_name")
    private String name;

    @JsonProperty("ward_id")
    private Long wardId;
}
