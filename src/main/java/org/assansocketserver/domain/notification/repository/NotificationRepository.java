package org.assansocketserver.domain.notification.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.assansocketserver.domain.notification.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByCategory(String category);
}
