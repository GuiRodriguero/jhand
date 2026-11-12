package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.CashPokerSession;

import java.math.BigDecimal;
import java.time.Instant;

public record CashSessionResponse(String sessionId, String type, Integer handCount, Instant startTime, Instant endTime,
		BigDecimal netProfit, BigDecimal bigBlind) implements PokerSessionResponse {

	static CashSessionResponse of(CashPokerSession entity) {
		return new CashSessionResponse(entity.getSessionId(), entity.getType().name(), entity.getHandCount(),
				entity.getStartTime(), entity.getEndTime(), entity.getNetProfit(), entity.getBigBlind());
	}

}
