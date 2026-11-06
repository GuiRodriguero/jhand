package com.gui.jhand.hand.web;

import com.gui.jhand.hand.HandResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.TestBase.valid;
import static org.assertj.core.api.Assertions.assertThat;

class HandResultSummaryResponseTest {

	private HandResult entity;

	@BeforeEach
	void setUp() {
		entity = valid(HandResult.class);
	}

	@Test
	void should_instantiate() {
		assertThat(HandResultSummaryResponse.of(entity)).isEqualTo(new HandResultSummaryResponse(entity.getHandId(),
				entity.getSessionId(), entity.getPosition().name(), entity.getHeroCards().toString(),
				entity.getBoard().toString(), entity.getHandRank().name(), entity.getNetProfit(), entity.getTime()));
	}

	@Test
	void should_instantiate_with_null_values() {
		HandResult nullableValuesHandResult = HandResult.builder()
			.handId(entity.getHandId())
			.sessionId(entity.getSessionId())
			.netProfit(entity.getNetProfit())
			.time(entity.getTime())
			.build();

		assertThat(HandResultSummaryResponse.of(nullableValuesHandResult))
			.isEqualTo(new HandResultSummaryResponse(entity.getHandId(), entity.getSessionId(), null, null, null, null,
					entity.getNetProfit(), entity.getTime()));
	}

}
