package com.gui.jhand.equity;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.equity.EquityEvaluationWinner.*;
import static org.assertj.core.api.Assertions.assertThat;

class EquityEvaluationWinnerTest {

	@Test
	void should_resolve_winner_hero() {
		assertThat(EquityEvaluationWinner.resolveWinner(1, 2)).isEqualTo(HERO);
	}

	@Test
	void should_resolve_winner_villain() {
		assertThat(EquityEvaluationWinner.resolveWinner(2, 1)).isEqualTo(VILLAIN);
	}

	@Test
	void should_resolve_winner_tie() {
		assertThat(EquityEvaluationWinner.resolveWinner(1, 1)).isEqualTo(TIE);
	}

}
