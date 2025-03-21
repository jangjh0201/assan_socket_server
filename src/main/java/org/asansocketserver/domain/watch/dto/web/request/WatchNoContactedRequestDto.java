package org.asansocketserver.domain.watch.dto.web.request;

import lombok.Data;

import java.util.List;

@Data
public class WatchNoContactedRequestDto {
    private Long watchId;
    private List<Long> noContactIds;
}
