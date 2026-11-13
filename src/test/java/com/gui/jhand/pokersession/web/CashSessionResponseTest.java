package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.CashPokerSession;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class CashSessionResponseTest {

	@Test
	void should_map_all_fields_from_cash_session() {
		CashPokerSession entity = valid(CashPokerSession.class);

		assertThat(CashSessionResponse.of(entity))
			.isEqualTo(new CashSessionResponse(entity.getSessionId(), entity.getType().name(), entity.getHandCount(),
					entity.getStartTime(), entity.getEndTime(), entity.getNetProfit(), entity.getBigBlind()));
	}

}
