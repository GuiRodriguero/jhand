package com.gui.jhand.hand;

import com.gui.jhand.core.poker.StartingHand;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.core.poker.Rank.*;
import static org.assertj.core.api.Assertions.assertThat;

class StartingHandConverterTest {

	private final StartingHandConverter converter = new StartingHandConverter();

	@Test
	void should_convert_to_databse_column() {
		StartingHand hand = new StartingHand(ACE, EIGHT, true);

		assertThat(converter.convertToDatabaseColumn(hand)).isEqualTo(hand.toString());
	}

	@Test
	void should_convert_to_databse_column_when_null() {
		assertThat(converter.convertToDatabaseColumn(null)).isNull();
	}

	@Test
	void should_convert_to_entity_attribute() {
		String hand = "AKs";

		assertThat(converter.convertToEntityAttribute(hand)).isEqualTo(new StartingHand(ACE, KING, true));
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
