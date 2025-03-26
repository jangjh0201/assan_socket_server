package org.assansocketserver.domain.notification.entity.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoData extends NotificationData {
    @JsonProperty("info_id")
    private Long id;
    @JsonProperty("info_name")
    private String name;
}