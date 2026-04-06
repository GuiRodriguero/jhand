package com.gui.jhand.core.poker;

import org.junit.jupiter.api.Test;

import static com.gui.jhand.core.poker.Rank.*;
import static com.gui.jhand.core.poker.StartingHand.fromDb;
import static com.gui.jhand.core.poker.StartingHand.fromHandHistory;
import static org.assertj.core.api.Assertions.assertThat;

class StartingHandTest {

	private final StartingHand suitedHand = new StartingHand(ACE, EIGHT, true);

	private final StartingHand offsuitedHand = new StartingHand(SEVEN, FOUR, false);

	private final StartingHand pocketPairHand = new StartingHand(ACE, ACE, false);

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
	void should_instantiate_from_db_string_suited() {
		assertThat(fromDb("A8s")).isEqualTo(suitedHand);
	}

	@Test
	void should_instantiate_from_db_string_offsuit() {
		assertThat(fromDb("74o")).isEqualTo(offsuitedHand);
	}

	@Test
	void should_instantiate_from_db_string_pocket_pair() {
		assertThat(fromDb("AA")).isEqualTo(pocketPairHand);
	}

	@Test
	void should_convert_to_string_suited() {
		assertThat(suitedHand.toString()).isEqualTo("A8s");
	}

	@Test
	void should_convert_to_string_offsuit() {
		assertThat(offsuitedHand.toString()).isEqualTo("74o");
	}

	@Test
	void should_convert_to_string_pocket_pair() {
		assertThat(pocketPairHand.toString()).isEqualTo("AA");
	}

}
