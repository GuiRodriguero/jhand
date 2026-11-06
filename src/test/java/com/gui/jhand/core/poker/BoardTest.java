package com.gui.jhand.core.poker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoardTest {

	@Test
	void should_instantiate_from_string() {
		String boardAsString = "Ts 8s 2h Kd Qd";
		String[] cardsAsStrings = boardAsString.trim().split("\\s+");

		assertThat(Board.fromString(boardAsString))
			.isEqualTo(new Board(Arrays.stream(cardsAsStrings).map(Card::fromHandHistory).toList()));
	}

	@Test
	void should_instantiate_from_cards() {
		assertThat(Board.of(Card.fromHandHistory("As"), Card.fromHandHistory("2s")).cards())
			.containsExactly(Card.fromHandHistory("As"), Card.fromHandHistory("2s"));
	}

	@Test
	void should_convert_to_string() {
		assertThat(Board
			.of(Card.fromHandHistory("As"), Card.fromHandHistory("2s"), Card.fromHandHistory("3s"),
					Card.fromHandHistory("4s"))
			.toString()).isEqualTo("As 2s 3s 4s");
	}

	@Test
	void should_check_if_object_is_equal() {
		assertThat(Board.of(Card.fromHandHistory("As"), Card.fromHandHistory("2s"), Card.fromHandHistory("3s"),
				Card.fromHandHistory("4s")))
			.isEqualTo(Board.of(Card.fromHandHistory("As"), Card.fromHandHistory("2s"), Card.fromHandHistory("3s"),
					Card.fromHandHistory("4s")));

		assertThat(Board.of()).isEqualTo(Board.of());
	}

	@Test
	void should_have_same_hash_code_when_equal() {
		assertThat(Board.fromString("As 2s 3s")).hasSameHashCodeAs(Board.fromString("As 2s 3s"));
	}

	@Test
	void should_not_be_affected_by_changes_on_source_list() {
		List<Card> source = new ArrayList<>(List.of(Card.fromHandHistory("As")));
		Board board = new Board(source);

		source.add(Card.fromHandHistory("Kd"));

		assertThat(board.cards()).containsExactly(Card.fromHandHistory("As"));
	}

	@Test
	void should_not_allow_modifying_cards() {
		Board board = Board.fromString("As 2s 3s");

		assertThatThrownBy(() -> board.cards().add(Card.fromHandHistory("Kd")))
			.isInstanceOf(UnsupportedOperationException.class);
	}

}
