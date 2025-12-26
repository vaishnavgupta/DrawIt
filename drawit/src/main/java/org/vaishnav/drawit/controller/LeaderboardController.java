package org.vaishnav.drawit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.vaishnav.drawit.dto.LeaderboardEntryDto;
import org.vaishnav.drawit.dto.MatchLeaderboardDto;
import org.vaishnav.drawit.service.LeaderboardService;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/global")
    @ResponseStatus(HttpStatus.OK)
    public List<LeaderboardEntryDto> getGlobalLeaderboard(){
        return leaderboardService.findGlobalLeaderboard();
    }

    @GetMapping("/match/{matchId}")
    @ResponseStatus(HttpStatus.OK)
    public MatchLeaderboardDto matchLeaderboard(
            @PathVariable Long matchId
    ) {
        return leaderboardService.findMatchLeaderboard(matchId);
    }
}
