package org.asansocketserver.domain.notification.mongorepository;

import org.asansocketserver.domain.notification.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationMongoRepository extends MongoRepository<Notification, String> {

    @Query(value = "{'timestamp': { $gte: ?0, $lte: ?1 }}", count = true)
    long countByTimestamp(LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query(value = "{'timestamp': { $gte: ?0, $lt: ?1 }}")
    List<Notification> findALLByTimestamp(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
