package org.vaishnav.drawit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaishnav.drawit.dto.LeaderboardEntryDto;
import org.vaishnav.drawit.dto.MatchLeaderboardDto;
import org.vaishnav.drawit.repository.PlayerScoreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final PlayerScoreRepository playerScoreRepository;

    public List<LeaderboardEntryDto> findGlobalLeaderboard(){
        return playerScoreRepository.findGlobalLeaderboard();
    }

    public MatchLeaderboardDto findMatchLeaderboard(Long matchId){
        List<LeaderboardEntryDto> matchLeaderboard = playerScoreRepository.findMatchLeaderboard(matchId);
        return new MatchLeaderboardDto(matchId, matchLeaderboard);
    }
}
