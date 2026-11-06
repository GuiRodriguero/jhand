package com.gui.jhand.pokersession.web;

import com.gui.jhand.pokersession.PokerSession;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class PokerSessionResponseTest {

	@Test
	void should_instantiate() {
		PokerSession entity = valid(PokerSession.class);

		assertThat(PokerSessionResponse.of(entity))
			.isEqualTo(new PokerSessionResponse(entity.getSessionId(), entity.getType().name(), entity.getNetProfit()));
	}

	@Test
	void should_instantiate_with_null_type() {
		PokerSession entity = PokerSession.builder().sessionId("My Session").build();

		assertThat(PokerSessionResponse.of(entity)).isEqualTo(new PokerSessionResponse("My Session", null, null));
	}

}
