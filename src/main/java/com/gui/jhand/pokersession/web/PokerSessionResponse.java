package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.PokerSession;

import java.math.BigDecimal;

public record PokerSessionResponse(String sessionId, String type, BigDecimal netProfit) {

	public static PokerSessionResponse of(PokerSession entity) {
		return new PokerSessionResponse(entity.getSessionId(),
				entity.getType() != null ? entity.getType().name() : null, entity.getNetProfit());
	}

}
