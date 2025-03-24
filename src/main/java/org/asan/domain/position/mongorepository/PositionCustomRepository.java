package org.asan.domain.position.mongorepository;

import org.asan.domain.position.entity.PositionData;

public interface PositionCustomRepository {
    void updatePosition(final Long watchId, final PositionData positionData);
}
