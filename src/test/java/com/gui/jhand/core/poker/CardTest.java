package com.gui.jhand.core.poker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.gui.jhand.core.poker.Rank.ACE;
import static com.gui.jhand.core.poker.Suit.SPADES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardTest {

	@Test
	void should_instantiate_from_hand_history_string() {
		assertThat(Card.fromHandHistory("As")).isEqualTo(new Card(ACE, SPADES));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "A", "Asd", "Xs", "Az" })
	void should_throw_exception_for_invalid_card(String card) {
		assertThatThrownBy(() -> Card.fromHandHistory(card)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void should_convert_to_string() {
		assertThat(new Card(ACE, SPADES).toString()).isEqualTo(ACE.getSymbol().concat(SPADES.getSymbol()));
	}

}
