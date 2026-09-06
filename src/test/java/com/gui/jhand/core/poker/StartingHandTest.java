package com.gui.jhand.core.poker;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.core.poker.Rank.*;
import static com.gui.jhand.core.poker.StartingHand.fromDb;
import static com.gui.jhand.core.poker.StartingHand.fromHandHistory;
import static com.gui.jhand.core.poker.Suit.*;
import static org.assertj.core.api.Assertions.assertThat;

class StartingHandTest {

	private final StartingHand suitedHand = new StartingHand(new Card(ACE, SPADES), new Card(EIGHT, SPADES));

	private final StartingHand offsuitedHand = new StartingHand(new Card(SEVEN, SPADES), new Card(FOUR, CLUBS));

	private final StartingHand pocketPairHand = new StartingHand(new Card(ACE, HEARTS), new Card(ACE, DIAMONDS));

	@Test
	void should_instantiate_from_hand_history_string_suited() {
		assertThat(fromHandHistory("As 8s")).isEqualTo(suitedHand);
	}

	@Test
	void should_instantiate_from_hand_history_string_offsuit() {
		assertThat(fromHandHistory("7s 4c")).isEqualTo(offsuitedHand);
	}

	@Test
	void should_instantiate_from_hand_history_string_pocket_pair() {
		assertThat(fromHandHistory("Ah Ad")).isEqualTo(pocketPairHand);
	}

	@Test
	void should_instantiate_from_db_string_with_suits() {
		assertThat(fromDb("As Kh")).isEqualTo(new StartingHand(new Card(ACE, SPADES), new Card(KING, HEARTS)));
	}

	@Test
	void should_convert_to_string_suited() {
		assertThat(suitedHand.toString()).isEqualTo("As 8s");
	}

	@Test
	void should_convert_to_string_offsuit() {
		assertThat(offsuitedHand.toString()).isEqualTo("7s 4c");
	}

	@Test
	void should_convert_to_string_pocket_pair() {
		assertThat(pocketPairHand.toString()).isEqualTo("Ah Ad");
	}

}
