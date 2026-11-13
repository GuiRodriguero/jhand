package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.CashPokerSession;
import com.gui.jhand.pokersession.PokerSession;
import com.gui.jhand.pokersession.TournamentPokerSession;

public sealed interface PokerSessionResponse permits CashSessionResponse, TournamentSessionResponse {

	static PokerSessionResponse of(PokerSession entity) {
		return switch (entity) {
			case CashPokerSession cash -> CashSessionResponse.of(cash);
			case TournamentPokerSession tournament -> TournamentSessionResponse.of(tournament);
			default -> throw new IllegalStateException("Unsupported session type: " + entity.getType());
		};
	}

}
