package com.gui.jhand.hand.web;

import com.gui.jhand.hand.HandResult;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public record HandResultSummaryResponse(String handId, String sessionId, String position, String heroCards,
		String board, String handRank, BigDecimal netProfit, BigDecimal netProfitInBb, BigDecimal totalPot,
		Instant date) {

	public static HandResultSummaryResponse of(HandResult entity) {
		return new HandResultSummaryResponse(entity.getHandId(), entity.getSessionId(),
				entity.getPosition() != null ? entity.getPosition().name() : null,
				Objects.toString(entity.getHeroCards(), null), Objects.toString(entity.getBoard(), null),
				entity.getHandRank() != null ? entity.getHandRank().name() : null, entity.getNetProfit(),
				entity.getNetProfitInBb(), entity.getTotalPot(), entity.getTime());
	}

}
