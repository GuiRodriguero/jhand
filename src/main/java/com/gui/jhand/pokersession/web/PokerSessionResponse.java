package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.PokerSession;

import java.math.BigDecimal;
import java.time.Instant;

public record PokerSessionResponse(String sessionId, String type, Integer handCount, Instant startTime,
		BigDecimal netProfit) {

	public static PokerSessionResponse of(PokerSession entity) {
		return new PokerSessionResponse(entity.getSessionId(),
				entity.getType() != null ? entity.getType().name() : null, entity.getHandCount(), entity.getStartTime(),
				entity.getNetProfit());
	}

}
