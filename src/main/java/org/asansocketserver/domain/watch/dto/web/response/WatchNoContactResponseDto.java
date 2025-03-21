package org.asansocketserver.domain.watch.dto.web.response;

import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class WatchNoContactResponseDto {
    private Long watchId;
    private List<Long> noContactIds;
}
