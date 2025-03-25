package org.assansocketserver.domain.risk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskTypeDTO {

    @JsonProperty("risk_id")
    private Long id;

    @JsonProperty("risk_name")
    private String name;
}
