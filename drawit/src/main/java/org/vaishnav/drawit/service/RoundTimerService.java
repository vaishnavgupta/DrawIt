package org.vaishnav.drawit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.vaishnav.drawit.redis.RoomStateService;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RoundTimerService {

    private final RoomStateService redisService;
    private final SimpMessagingTemplate template;

    //for multiple active rooms
    private final Set<String> activeRounds = ConcurrentHashMap.newKeySet();

    public void startTimer(String roomId) {
        activeRounds.add(roomId);
    }

    public void stopTimer(String roomId) {
        activeRounds.remove(roomId);
    }

    @Scheduled(fixedRate = 1000)
    public void tick(){
        for(String roomId : activeRounds){
            int timeLeft = redisService.decrementTimer(roomId);

            template.convertAndSend(
                    "/topic/room/" + roomId + "/timer",
                    timeLeft
            );

            if(timeLeft <= 0){
                endRound(roomId);
            }
        }
    }

    public void endRound(String roomId){
        stopTimer(roomId);

        String word = redisService.getWord(roomId);

        template.convertAndSend("/topic/room/" + roomId + "/system",
                "Round ended Word was: " + word
        );

        redisService.resetRound(roomId);
    }


}
