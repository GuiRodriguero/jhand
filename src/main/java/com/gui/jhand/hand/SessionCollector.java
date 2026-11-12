package com.gui.jhand.hand;

import com.gui.jhand.action.Action;
import com.gui.jhand.pokersession.PokerSession;
import com.gui.jhand.pokersession.SessionType;

import java.time.Instant;
import java.util.List;

interface SessionCollector {

	void accept(List<Action> actions, HandResult hand);

	PokerSession toSession(String sessionId, int handCount, Instant startTime);

	static SessionCollector of(SessionType type, String heroName) {
		return switch (type) {
			case CASH_GAME -> new CashSessionCollector();
			case TOURNAMENT -> TournamentSessionCollector.of(heroName);
		};
	}

}
