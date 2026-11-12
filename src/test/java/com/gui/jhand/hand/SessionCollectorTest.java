package com.gui.jhand.hand;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.pokersession.SessionType.CASH_GAME;
import static com.gui.jhand.pokersession.SessionType.TOURNAMENT;
import static org.assertj.core.api.Assertions.assertThat;

class SessionCollectorTest {

	@Test
	void should_create_cash_session_collector_for_cash_game() {
		assertThat(SessionCollector.of(CASH_GAME, "GuiRodri2013")).isInstanceOf(CashSessionCollector.class);
	}

	@Test
	void should_create_tournament_session_collector_for_tournament() {
		assertThat(SessionCollector.of(TOURNAMENT, "GuiRodri2013")).isInstanceOf(TournamentSessionCollector.class);
	}

}
