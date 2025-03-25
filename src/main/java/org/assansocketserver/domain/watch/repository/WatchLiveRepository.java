package org.assansocketserver.domain.watch.repository;

import org.assansocketserver.domain.watch.entity.WatchLive;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WatchLiveRepository extends CrudRepository<WatchLive, Long> {
    List<WatchLive> findAllByLive(boolean live);
}
