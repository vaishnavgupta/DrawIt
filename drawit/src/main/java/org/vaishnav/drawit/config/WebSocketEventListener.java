package org.vaishnav.drawit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.vaishnav.drawit.redis.RoomStateService;
import org.vaishnav.drawit.service.GameService;
import org.vaishnav.drawit.util.SessionRegistry;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final RoomStateService roomStateService;
    private final SessionRegistry sessionRegistry;
    private final GameService gameService;

    @EventListener
    public void handleConnect(SessionConnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = accessor.getSessionId();
        String username = accessor.getFirstNativeHeader("username");
        String roomId   = accessor.getFirstNativeHeader("roomId");

        System.out.println("NATIVE HEADERS → " + accessor.toNativeHeaderMap());


        System.out.println("CONNECT EVENT → user=" + username + ", room=" + roomId + " session=" + sessionId);

        if(username == null || roomId == null){
            System.out.println("CONNECT ignored (missing data)");
            return;
        }

        //saving in sessions
        sessionRegistry.register(sessionId, username, roomId);

        //add in Redis
        roomStateService.addPlayer(roomId, username);

        gameService.notifyUserJoined(roomId, username);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event){
        String sessionId = event.getSessionId();
        String username = sessionRegistry.getUsername(sessionId);
        String roomId = sessionRegistry.getRoomId(sessionId);

        if(username == null || roomId == null){
            return;
        }

        roomStateService.removePlayer(roomId, username);

        String currentDrawer = roomStateService.getDrawer(roomId);
        if (username.equals(currentDrawer)) {
            handleDrawerLeft(roomId);
        }

        gameService.notifyUserLeft(roomId, username);

        sessionRegistry.remove(sessionId);

        if (roomStateService.getPlayers(roomId).isEmpty()) {
            roomStateService.clearRoom(roomId);
        }
    }

    private void handleDrawerLeft(String roomId) {
        roomStateService.setWord(roomId, null);
        roomStateService.setTimer(roomId, 0);

        gameService.notifyDrawerLeft(roomId);
    }
}
