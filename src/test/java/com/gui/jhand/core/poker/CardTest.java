package com.gui.jhand.core.poker;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.core.poker.Rank.ACE;
import static com.gui.jhand.core.poker.Suit.SPADES;
import static org.assertj.core.api.Assertions.assertThat;

class CardTest {

	@Test
	void should_instantiate_from_hand_history_string() {
		assertThat(Card.fromHandHistory("As")).isEqualTo(new Card(ACE, SPADES));
	}

	@Test
	void should_convert_to_string() {
		assertThat(new Card(ACE, SPADES).toString()).isEqualTo(ACE.getSymbol().concat(SPADES.getSymbol()));
	}

}
