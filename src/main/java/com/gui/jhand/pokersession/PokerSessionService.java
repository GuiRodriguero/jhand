package com.gui.jhand.pokersession;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PokerSessionService {

	private final PokerSessionRepository repository;

	public PokerSession create(String sessionId, BigDecimal netProfit) {
		return repository.save(PokerSession.of(sessionId, netProfit));
	}

}
