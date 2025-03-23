package org.asan.auth.dto;

import java.util.List;
import org.asan.domain.risk.dto.RiskTypeDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 제외
public class AccountDTO {

    @JsonProperty("account_id")
    private Long id;

    @JsonProperty("account_name")
    private String name;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("hospital_name")
    private String hospitalName;

    @JsonProperty("ward_name")
    private String wardName;

    @JsonProperty("manager_name")
    private String managerName;

    @JsonProperty("manager_tel")
    private String managerTel;

    @JsonProperty("manager_email")
    private String managerEmail;

    @JsonProperty("risks")
    private List<RiskTypeDTO> riskList;

}
