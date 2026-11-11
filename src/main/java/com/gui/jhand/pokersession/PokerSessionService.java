package com.gui.jhand.pokersession;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PokerSessionService {

	private final PokerSessionRepository repository;

	public PokerSession create(String sessionId, SessionType type, int handCount, Instant startTime,
			BigDecimal netProfit) {
		return repository.save(PokerSession.of(sessionId, type, handCount, startTime, netProfit));
	}

}
