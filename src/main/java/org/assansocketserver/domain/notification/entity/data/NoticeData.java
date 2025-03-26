package org.assansocketserver.domain.notification.entity.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeData extends NotificationData {
    @JsonProperty("notice_id")
    private Long id;
    @JsonProperty("notice_title")
    private String title;
    @JsonProperty("notice_author")
    private String author;
}