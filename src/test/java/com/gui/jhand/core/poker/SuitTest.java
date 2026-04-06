package com.gui.jhand.core.poker;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.core.poker.Suit.CLUBS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SuitTest {

	@Test
	void should_instantiate_from_symbol() {
		assertThat(Suit.fromSymbol("c")).isEqualTo(CLUBS);
	}

	@Test
	void should_throw_exception_when_trying_to_instantiate_from_symbol() {
		assertThatThrownBy(() -> Suit.fromSymbol("g")).isInstanceOf(IllegalArgumentException.class);
	}

}
