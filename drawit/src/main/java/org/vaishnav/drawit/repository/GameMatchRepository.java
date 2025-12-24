package org.vaishnav.drawit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vaishnav.drawit.entity.GameMatch;

@Repository
public interface GameMatchRepository extends JpaRepository<GameMatch, Long> {
}
