package org.asan.domain.sector.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SectorType {
    PUBLIC("허용 구역"),
    RESTRICTED("환자 금지 구역"),
    MALE("남성 구역"),
    FEMALE("여성 구역");

    private final String name;

    SectorType(String name) {
        this.name = name;
    }

    @JsonValue // JSON 변환 시 name 필드를 반환
    public String getName() {
        return name;
    }

    public static SectorType fromName(String name) {
        for (SectorType type : SectorType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
