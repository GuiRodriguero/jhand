package com.gui.jhand.pokersession.web;

import com.gui.jhand.core.utils.ChipsUtils;
import com.gui.jhand.pokersession.CashPokerSession;
import com.gui.jhand.pokersession.TournamentPokerSession;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.gui.jhand.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class PokerSessionResponseTest {

	@Test
	void should_instantiate_from_tournament_session() {
		TournamentPokerSession entity = valid(TournamentPokerSession.class);

		assertThat(PokerSessionResponse.of(entity))
			.isEqualTo(new PokerSessionResponse(entity.getSessionId(), entity.getType().name(), entity.getHandCount(),
					entity.getStartTime(), entity.getEndTime(), entity.getNetProfit(), entity.getBuyIn(),
					entity.getPrize(), entity.getFinishPosition(), entity.getRealMoney()));
	}

	@Test
	void should_instantiate_from_cash_session_without_tournament_fields() {
		Instant startTime = Instant.ofEpochSecond(1771774381);
		Instant endTime = startTime.plusSeconds(3600);
		CashPokerSession entity = CashPokerSession.of("My Cash", 42, startTime, endTime, ChipsUtils.of(1.25));

		assertThat(PokerSessionResponse.of(entity)).isEqualTo(new PokerSessionResponse("My Cash", "CASH_GAME", 42,
				startTime, endTime, ChipsUtils.of(1.25), null, null, null, null));
	}

	@Test
	void should_instantiate_with_null_values() {
		CashPokerSession entity = valid(CashPokerSession.class);
		CashPokerSession withNulls = CashPokerSession.of(entity.getSessionId(), null, null, null, null);

		assertThat(PokerSessionResponse.of(withNulls)).isEqualTo(new PokerSessionResponse(entity.getSessionId(),
				"CASH_GAME", null, null, null, null, null, null, null, null));
	}

}
