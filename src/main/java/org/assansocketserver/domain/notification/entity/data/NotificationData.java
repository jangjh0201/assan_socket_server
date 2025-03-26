package org.assansocketserver.domain.notification.entity.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class NotificationData {
    @JsonProperty("message")
    private String message;
    @JsonProperty("timestamp")
    private String timestamp;
}
