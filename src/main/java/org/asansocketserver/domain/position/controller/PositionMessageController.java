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

@RequiredArgsConstructor
@RestController
public class PositionMessageController {
    private final PositionService positionService;
    private final SimpMessageSendingOperations sendingOperations;


    @MessageMapping("/position")
    public void sendAccelerometer(@Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
                                  @Payload final PosDataDTO request) throws Exception {
        String destination = "/queue/sensor/" + simpSessionAttributes.get("watchId");

        PositionResponseDto responseDto = positionService.receiveData(request,destination);
        sendingOperations.convertAndSend(destination, SocketBaseResponse.of(MessageType.POSITION, responseDto));
    }


}
