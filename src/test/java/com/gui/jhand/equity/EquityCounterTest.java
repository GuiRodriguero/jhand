package com.gui.jhand.equity;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.equity.EquityEvaluationWinner.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class EquityCounterTest {

	@Test
	void should_add_and_convert_to_result() {
		EquityCounter counter = new EquityCounter();

		assertThatCode(() -> counter.add(HERO)).doesNotThrowAnyException();
		assertThatCode(() -> counter.add(VILLAIN)).doesNotThrowAnyException();
		assertThatCode(() -> counter.add(TIE)).doesNotThrowAnyException();
		assertThat(counter.toResult()).isEqualTo(EquityResult.of(1, 1, 1));
	}

}
