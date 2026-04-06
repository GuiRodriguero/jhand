package com.gui.jhand.core.poker;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.core.poker.Rank.KING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RankTest {

	@Test
	void should_instantiate_from_symbol() {
		assertThat(Rank.fromSymbol("K")).isEqualTo(KING);
	}

	@Test
	void should_throw_exception_when_trying_to_instantiate_from_symbol() {
		assertThatThrownBy(() -> Rank.fromSymbol("G")).isInstanceOf(IllegalArgumentException.class);
	}

}
