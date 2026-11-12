package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.PokerSession;
import com.gui.jhand.pokersession.TournamentPokerSession;

import java.math.BigDecimal;
import java.time.Instant;

public record PokerSessionResponse(String sessionId, String type, Integer handCount, Instant startTime, Instant endTime,
		BigDecimal netProfit, BigDecimal buyIn, BigDecimal prize, Integer finishPosition, Boolean realMoney) {

	public static PokerSessionResponse of(PokerSession entity) {
		String type = entity.getType() != null ? entity.getType().name() : null;

		if (entity instanceof TournamentPokerSession tournament) {
			return new PokerSessionResponse(entity.getSessionId(), type, entity.getHandCount(), entity.getStartTime(),
					entity.getEndTime(), entity.getNetProfit(), tournament.getBuyIn(), tournament.getPrize(),
					tournament.getFinishPosition(), tournament.getRealMoney());
		}

		return new PokerSessionResponse(entity.getSessionId(), type, entity.getHandCount(), entity.getStartTime(),
				entity.getEndTime(), entity.getNetProfit(), null, null, null, null);
	}

}
