package org.assansocketserver.domain.position.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.assansocketserver.domain.position.dto.request.PosDataDTO;
import org.assansocketserver.domain.position.dto.response.PositionResponseDto;
import org.assansocketserver.domain.position.service.PositionService;
import org.assansocketserver.socket.dto.MessageType;
import org.assansocketserver.socket.dto.SocketBaseResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Tag(name = "Position WebSocket API", description = "WebSocket을 이용한 위치 데이터 처리 API")
@RequiredArgsConstructor
@RestController
public class PositionMessageController {
    private final PositionService positionService;
    private final SimpMessageSendingOperations sendingOperations;
    private final RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "워치의 센서 데이터 전송", description = "워치에서 전송한 가속도계 데이터를 받아 처리하고, 처리된 위치 데이터를 WebSocket을 통해 반환합니다.")
    @MessageMapping("/position")
    public void sendAccelerometer(
            @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
            @Payload final PosDataDTO request) throws Exception {
        Object watchIdObj = simpSessionAttributes.get("watchId");
        if (watchIdObj != null) {
            Long watchId = Long.parseLong(watchIdObj.toString());
            refreshWatchLiveTtl(watchId);
        }

        String destination = "/queue/sensor/" + watchIdObj;

        PositionResponseDto responseDto = positionService.receiveData(request, destination);
        sendingOperations.convertAndSend(destination, SocketBaseResponse.of(MessageType.POSITION, responseDto));
    }

    private void refreshWatchLiveTtl(Long watchId) {
        String redisKey = "watch:" + watchId;
        redisTemplate.expire(redisKey, Duration.ofSeconds(100)); // TTL 연장
    }
    
}
