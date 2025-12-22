package org.vaishnav.drawit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.vaishnav.drawit.service.GameService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {

    private final SimpMessagingTemplate template;   //sends message manually
    private final GameService gameService;

    @MessageMapping("/room/{roomId}/draw")
    public void handleDraw(
            @DestinationVariable String roomId,
            String message,
            Principal principal
    ){
        if(!gameService.isDrawer(roomId, principal.getName())){
            return;
        }

        template.convertAndSend(
                "/topic/room/" + roomId + "/draw",
                message
        );
    }

    @MessageMapping("/room/{roomId}/guess")
    public void handleSend(
            @DestinationVariable String roomId,
            String message,
            Principal principal
    ){
        String result = gameService.processGuess(
                roomId,
                message,
                principal.getName()
        );

        template.convertAndSend(
                "/topic/room/" + roomId + "/guess",
                result
        );
    }

    @MessageMapping("/room/{roomId}/start")
    public void startGame(@DestinationVariable String roomId){
        gameService.startRound(roomId);
    }

}
