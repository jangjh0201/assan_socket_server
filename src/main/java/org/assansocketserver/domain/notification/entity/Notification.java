package org.assansocketserver.domain.notification.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "notifications") // MongoDB 컬렉션 지정
public class Notification {
    @Id
    private String id; // MongoDB의 기본 ID (ObjectId)
    private String category;
    private Map<String, Object> data;
    private boolean isRead;

    public void markAsRead() {
        this.isRead = true;
    }
}
