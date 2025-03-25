package org.assansocketserver.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 관리자/슈퍼 전용 응답
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String token; // "Bearer eyJhbGciOi..."
    private String account_name; // "연중병원" 등
    private String role_name; // "ROLE_ADMIN" / "ROLE_SUPER"
}