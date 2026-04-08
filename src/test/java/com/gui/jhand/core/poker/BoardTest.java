package com.gui.jhand.core.poker;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

	@Test
	void should_instantiate_from_string() {
		String boardAsString = "Ts 8s 2h Kd Qd";
		String[] cardsAsStrings = boardAsString.trim().split("\\s+");

		assertThat(Board.fromString(boardAsString))
			.isEqualTo(new Board(Arrays.stream(cardsAsStrings).map(Card::fromHandHistory).toArray(Card[]::new)));
	}

	@Test
	void should_convert_to_string() {
		assertThat(new Board(Card.fromHandHistory("As"), Card.fromHandHistory("2s"), Card.fromHandHistory("3s"),
				Card.fromHandHistory("4s"))
			.toString()).isEqualTo("As 2s 3s 4s");
	}

	@Test
	void should_check_if_object_is_equal() {
		assertThat(new Board(Card.fromHandHistory("As"), Card.fromHandHistory("2s"), Card.fromHandHistory("3s"),
				Card.fromHandHistory("4s")))
			.isEqualTo(new Board(Card.fromHandHistory("As"), Card.fromHandHistory("2s"), Card.fromHandHistory("3s"),
					Card.fromHandHistory("4s")));

		assertThat(new Board()).isEqualTo(new Board());
	}

	@Test
	void should_check_if_object_is_equal_when_object_is_null() {
		assertThat(new Board()).isNotEqualTo(null);
	}

}
