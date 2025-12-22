package org.vaishnav.drawit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaishnav.drawit.redis.RoomStateService;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class DrawerService {

    private final RoomStateService roomStateService;

    public String selectRandomDrawer(String roomId){
         Set<Object> players = roomStateService.getPlayers(roomId);

         if(players == null || players.isEmpty()){
             throw new IllegalStateException("No players in room");
         }

         int idx = ThreadLocalRandom.current().nextInt(players.size());
         return players.stream().skip(idx).findFirst().get().toString();
    }

}
