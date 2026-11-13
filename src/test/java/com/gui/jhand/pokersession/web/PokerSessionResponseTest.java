package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.CashPokerSession;
import com.gui.jhand.pokersession.PokerSession;
import com.gui.jhand.pokersession.TournamentPokerSession;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PokerSessionResponseTest {

	@Test
	void should_create_cash_response_from_cash_session() {
		CashPokerSession entity = valid(CashPokerSession.class);

		assertThat(PokerSessionResponse.of(entity)).isEqualTo(CashSessionResponse.of(entity));
	}

	@Test
	void should_create_tournament_response_from_tournament_session() {
		TournamentPokerSession entity = valid(TournamentPokerSession.class);

		assertThat(PokerSessionResponse.of(entity)).isEqualTo(TournamentSessionResponse.of(entity));
	}

	@Test
	void should_throw_for_unsupported_session_type() {
		PokerSession entity = new PokerSession() {
		};

		assertThatThrownBy(() -> PokerSessionResponse.of(entity)).isInstanceOf(IllegalStateException.class);
	}

}
