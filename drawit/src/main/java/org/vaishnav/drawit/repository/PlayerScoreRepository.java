package org.vaishnav.drawit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vaishnav.drawit.entity.PlayerScore;

@Repository
public interface PlayerScoreRepository extends JpaRepository<PlayerScore, Long> {
}
