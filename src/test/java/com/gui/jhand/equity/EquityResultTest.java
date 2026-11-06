package com.gui.jhand.equity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EquityResultTest {

	@Test
	void should_instantiate() {
		assertThat(EquityResult.of(4, 4, 0)).isEqualTo(new EquityResult(0.5, 0.5, 0.0, 4, 4, 0, 8));
	}

}
