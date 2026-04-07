package com.gui.jhand.hand;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.Card;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.core.poker.Board.fromString;
import static org.assertj.core.api.Assertions.assertThat;

class BoardConverterTest {

	private final BoardConverter converter = new BoardConverter();

	private final Board defaultBoard = new Board(Card.fromHandHistory("As"), Card.fromHandHistory("2s"),
			Card.fromHandHistory("3s"), Card.fromHandHistory("4s"));

	@Test
	void should_convert_to_databse_column() {
		assertThat(converter.convertToDatabaseColumn(defaultBoard)).isEqualTo(defaultBoard.toString());
	}

	@Test
	void should_convert_to_databse_column_when_null() {
		assertThat(converter.convertToDatabaseColumn(null)).isNull();
	}

	@Test
	void should_convert_to_entity_attribute() {
		String board = "As 2s 3s 4s";

		assertThat(converter.convertToEntityAttribute(board)).isEqualTo(fromString(board));
	}

	@Test
	void should_convert_to_entity_attribute_when_db_column_is_empty() {
		assertThat(converter.convertToEntityAttribute("")).isNull();
	}

	@Test
	void should_convert_to_entity_attribute_when_db_column_is_empty_null() {
		assertThat(converter.convertToEntityAttribute(null)).isNull();
	}

}
