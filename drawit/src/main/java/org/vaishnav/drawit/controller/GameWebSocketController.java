package org.vaishnav.drawit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.vaishnav.drawit.dto.DrawMessage;
import org.vaishnav.drawit.dto.GuessMessage;
import org.vaishnav.drawit.redis.RoomStateService;
import org.vaishnav.drawit.service.GameService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {

    private final SimpMessagingTemplate template;   //sends message manually
    private final GameService gameService;
    private final RoomStateService redisService;

    @MessageMapping("/room/{roomId}/draw")
    public void handleDraw(
            @DestinationVariable String roomId,
            DrawMessage message,
            Principal principal
    ){
        String username = principal.getName();
        String drawer = redisService.getDrawer(roomId);

        if(!username.equals(drawer)){
            return;         //Ignoring cheating attempts
        }

        if ("CLEAR".equals(message.getType())) {
            template.convertAndSend(
                    "/topic/room/" + roomId + "/draw",
                    message
            );
            return; // stop further processing
        }

        template.convertAndSend(
                "/topic/room/" + roomId + "/draw",
                message
        );

    }

    @MessageMapping("/room/{roomId}/guess")
    public void handleGuess(
            @DestinationVariable String roomId,
            GuessMessage message,
            Principal principal
    ){
        String username = principal.getName();

        gameService.processGuess(roomId, username, message.getGuess());
    }

    @MessageMapping("/room/{roomId}/start")
    public void startGame(@DestinationVariable String roomId){
        gameService.startRound(roomId);
    }

}
