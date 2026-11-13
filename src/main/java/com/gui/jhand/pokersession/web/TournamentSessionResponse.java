package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.TournamentPokerSession;

import java.math.BigDecimal;
import java.time.Instant;

public record TournamentSessionResponse(String sessionId, String type, Integer handCount, Instant startTime,
		Instant endTime, BigDecimal netProfit, BigDecimal buyIn, BigDecimal prize, Integer finishPosition,
		Boolean realMoney, String outcome) implements PokerSessionResponse {

	static TournamentSessionResponse of(TournamentPokerSession entity) {
		return new TournamentSessionResponse(entity.getSessionId(), entity.getType().name(), entity.getHandCount(),
				entity.getStartTime(), entity.getEndTime(), entity.getNetProfit(), entity.getBuyIn(), entity.getPrize(),
				entity.getFinishPosition(), entity.getRealMoney(), entity.getOutcome().name());
	}

}
