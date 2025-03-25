package org.assansocketserver.domain.position.mongorepository;

import org.assansocketserver.domain.position.entity.PositionData;

public interface PositionCustomRepository {
    void updatePosition(final Long watchId, final PositionData positionData);
}
