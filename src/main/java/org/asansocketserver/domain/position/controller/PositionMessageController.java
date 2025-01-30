package org.asansocketserver.domain.position.controller;

import lombok.RequiredArgsConstructor;
import org.asansocketserver.domain.position.dto.request.PosDataDTO;
import org.asansocketserver.domain.position.dto.response.PositionResponseDto;
import org.asansocketserver.domain.position.service.PositionService;
import org.asansocketserver.socket.dto.MessageType;
import org.asansocketserver.socket.dto.SocketBaseResponse;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Tag(name = "Position WebSocket API", description = "WebSocket을 이용한 위치 데이터 처리 API")
@RequiredArgsConstructor
@RestController
public class PositionMessageController {
    private final PositionService positionService;
    private final SimpMessageSendingOperations sendingOperations;

    @Operation(summary = "워치의 센서 데이터 전송", description = "워치에서 전송한 가속도계 데이터를 받아 처리하고, 처리된 위치 데이터를 WebSocket을 통해 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 처리 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @MessageMapping("/position")
    public void sendAccelerometer(
            @Parameter(description = "WebSocket 세션 속성", hidden = true) @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
            @Parameter(description = "워치에서 전송하는 센서 데이터") @Payload final PosDataDTO request) throws Exception {

        String destination = "/queue/sensor/" + simpSessionAttributes.get("watchId");

        PositionResponseDto responseDto = positionService.receiveData(request, destination);
        sendingOperations.convertAndSend(destination, SocketBaseResponse.of(MessageType.POSITION, responseDto));
    }
}
