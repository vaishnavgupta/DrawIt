package org.vaishnav.drawit.config;


import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class WebSocketUserInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String username = accessor.getFirstNativeHeader("username");
            String roomId   = accessor.getFirstNativeHeader("roomId");

            if (username == null || roomId == null) {
                throw new IllegalArgumentException("username or roomId missing");
            }

            accessor.setUser(new Principal() {
                @Override
                public String getName() {
                    return username;
                }
            });
        }

        return message;
    }
}
