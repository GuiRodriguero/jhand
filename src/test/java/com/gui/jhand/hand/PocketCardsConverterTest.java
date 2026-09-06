package com.gui.jhand.hand;

import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import org.junit.jupiter.api.Test;

import static com.gui.jhand.core.poker.Rank.*;
import static com.gui.jhand.core.poker.Suit.SPADES;
import static org.assertj.core.api.Assertions.assertThat;

class PocketCardsConverterTest {

	private final PocketCardsConverter converter = new PocketCardsConverter();

	@Test
	void should_convert_to_database_column() {
		PocketCards hand = new PocketCards(new Card(ACE, SPADES), new Card(EIGHT, SPADES));

		assertThat(converter.convertToDatabaseColumn(hand)).isEqualTo(hand.toString());
	}

	@Test
	void should_convert_to_databse_column_when_null() {
		assertThat(converter.convertToDatabaseColumn(null)).isNull();
	}

	@Test
	void should_convert_to_entity_attribute() {
		String hand = "As Ks";

		assertThat(converter.convertToEntityAttribute(hand))
			.isEqualTo(new PocketCards(new Card(ACE, SPADES), new Card(KING, SPADES)));
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
