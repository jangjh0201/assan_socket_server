package org.assansocketserver.auth.dto;

import java.util.List;

import org.assansocketserver.domain.risk.dto.RiskDTO;
import org.assansocketserver.domain.risk.entity.Risk;
import org.assansocketserver.domain.riskgroup.dto.RiskGroupDTO;
import org.assansocketserver.domain.riskgroup.entity.RiskGroup;
import org.assansocketserver.domain.ward.dto.MapDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserInfo {

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("ward_id")
    private Long wardId;

    @JsonProperty("ward_name")
    private String wardName;

    @JsonProperty("risks")
    private List<RiskDTO> risks;

    @JsonProperty("riskgroups")
    private List<RiskGroupDTO> riskgroups;

    @JsonProperty("map")
    private MapDTO map;
}
