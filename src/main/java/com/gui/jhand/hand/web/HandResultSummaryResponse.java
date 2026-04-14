package com.gui.jhand.hand.web;

import com.gui.jhand.hand.HandResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;

public record HandResultSummaryResponse(String handId, String sessionId, String heroCards, String handRank,
		BigDecimal netProfit, LocalDateTime date) {

	public static HandResultSummaryResponse of(HandResult entity) {
		return new HandResultSummaryResponse(entity.getHandId(), entity.getSessionId(),
				entity.getHeroCards().toString(), entity.getHandRank().name(), entity.getNetProfit(),
				ofInstant(entity.getTime(), systemDefault()));
	}

}