package org.assansocketserver.domain.notification.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationDTO {
    @JsonProperty("notification_id")
    private String id;
    @JsonProperty("notification_category")
    private String category;
    @JsonProperty("notification_data")
    private Map<String, Object> data;

    private boolean isRead;
}
