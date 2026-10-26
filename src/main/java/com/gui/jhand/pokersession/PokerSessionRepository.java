package com.gui.jhand.pokersession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface PokerSessionRepository extends JpaRepository<PokerSession, String>, JpaSpecificationExecutor<PokerSession> {

}
