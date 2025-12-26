package org.vaishnav.drawit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.vaishnav.drawit.dto.LeaderboardEntryDto;
import org.vaishnav.drawit.entity.PlayerScore;

import java.util.List;

@Repository
public interface PlayerScoreRepository extends JpaRepository<PlayerScore, Long> {

    //Global Leaderboard
    @Query("""
    SELECT new org.vaishnav.drawit.dto.LeaderboardEntryDto(
       ps.username,
       SUM(ps.score)
    )
    FROM PlayerScore ps
    GROUP BY ps.username
    ORDER BY SUM(ps.score) DESC
""")
    List<LeaderboardEntryDto> findGlobalLeaderboard();

    //Match Leaderboard
    @Query("""
    SELECT new org.vaishnav.drawit.dto.LeaderboardEntryDto(
       ps.username,
       ps.score
    )
    FROM PlayerScore ps
    WHERE ps.gameMatch.id = :matchId
    ORDER BY ps.score DESC
""")
    List<LeaderboardEntryDto> findMatchLeaderboard(@Param("matchId") Long matchId);

}
