package org.vaishnav.drawit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MatchLeaderboardDto {
    private Long matchId;
    private List<LeaderboardEntryDto> leaderboard;
}
