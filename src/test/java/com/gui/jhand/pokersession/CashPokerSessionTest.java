package com.gui.jhand.pokersession;

import com.gui.jhand.core.utils.ChipsUtils;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.gui.jhand.pokersession.SessionType.CASH_GAME;
import static org.assertj.core.api.Assertions.assertThat;

class CashPokerSessionTest {

	@Test
	void should_instantiate() {
		Instant startTime = Instant.ofEpochSecond(1771774381);

		CashPokerSession session = CashPokerSession.of("My Cash", 42, startTime, ChipsUtils.of(1.25));

		assertThat(session.getSessionId()).isEqualTo("My Cash");
		assertThat(session.getType()).isEqualTo(CASH_GAME);
		assertThat(session.getHandCount()).isEqualTo(42);
		assertThat(session.getStartTime()).isEqualTo(startTime);
		assertThat(session.getNetProfit()).isEqualTo(ChipsUtils.of(1.25));
	}

}
