package org.asan.domain.watch.repository;

import org.asan.domain.watch.entity.WatchLive;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WatchLiveRepository extends CrudRepository<WatchLive, Long> {
    List<WatchLive> findAllByLive(boolean live);
}
