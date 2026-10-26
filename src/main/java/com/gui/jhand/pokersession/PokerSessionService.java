package com.gui.jhand.pokersession;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.gui.jhand.pokersession.PokerSessionUtils.normalizeSessionId;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class PokerSessionService {

	private final PokerSessionRepository repository;

	public PokerSession create(String fileName, BigDecimal netProfit) {
		return repository.save(PokerSession.of(normalizeSessionId(fileName), netProfit));
	}

}
