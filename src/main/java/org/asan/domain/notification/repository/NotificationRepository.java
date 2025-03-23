package org.asan.domain.notification.repository;

import java.util.List;

import org.asan.domain.notification.entity.Notification;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByCategory(String category);
}
