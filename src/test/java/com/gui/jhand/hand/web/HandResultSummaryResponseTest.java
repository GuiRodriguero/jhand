package com.gui.jhand.hand.web;

import com.gui.jhand.hand.HandResult;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.TestBase.valid;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;

class HandResultSummaryResponseTest {

	private HandResult entity;

	@BeforeEach
	void setUp() {
		entity = valid(HandResult.class);
	}

	@Test
	void should_instantiate() {
		assertThat(HandResultSummaryResponse.of(entity)).isEqualTo(expected(entity));
	}

	@Test
	void should_instantiate_with_null_values() {
		HandResult nullableValuesHandResult = HandResult.builder()
			.handId(entity.getHandId())
			.sessionId(entity.getSessionId())
			.heroCards(entity.getHeroCards())
			.netProfit(entity.getNetProfit())
			.time(entity.getTime())
			.build();
		assertThat(HandResultSummaryResponse.of(nullableValuesHandResult))
			.isEqualTo(expected(nullableValuesHandResult));
	}

	private @NonNull HandResultSummaryResponse expected(HandResult entity) {
		return new HandResultSummaryResponse(entity.getHandId(), entity.getSessionId(),
				entity.getHeroCards().toString(), entity.getBoard() != null ? entity.getBoard().toString() : null,
				entity.getHandRank() != null ? entity.getHandRank().name() : null, entity.getNetProfit(),
				ofInstant(entity.getTime(), systemDefault()));
	}

}
