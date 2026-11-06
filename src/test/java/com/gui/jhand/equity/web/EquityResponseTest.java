package com.gui.jhand.equity.web;

import com.gui.jhand.equity.EquityResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EquityResponseTest {

	@Test
	void should_instantiate() {
		EquityResult result = EquityResult.of(30, 10, 4);

		assertThat(EquityResponse.of(result)).isEqualTo(new EquityResponse(result.heroEquity(), result.villainEquity(),
				result.tieProbability(), result.totalScenarios()));
	}

}
