package com.gui.jhand.equity.web;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.PocketCards;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EquityRequestTest {

	@Test
	void should_convert_to_domain_objects() {
		EquityRequest request = new EquityRequest(" As Kh ", "Qc Qd", "2c 7d Th");

		assertThat(request.toHeroCards()).isEqualTo(PocketCards.fromHandHistory("As Kh"));
		assertThat(request.toVillainCards()).isEqualTo(PocketCards.fromHandHistory("Qc Qd"));
		assertThat(request.toBoard()).isEqualTo(Board.fromString("2c 7d Th"));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "   " })
	void should_convert_blank_board_to_empty_board(String board) {
		assertThat(new EquityRequest("As Kh", "Qc Qd", board).toBoard()).isEqualTo(Board.of());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "   " })
	void should_throw_exception_when_hero_cards_are_missing(String heroCards) {
		assertThatThrownBy(() -> new EquityRequest(heroCards, "Qc Qd", null).toHeroCards())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Hero cards are required");
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "   " })
	void should_throw_exception_when_villain_cards_are_missing(String villainCards) {
		assertThatThrownBy(() -> new EquityRequest("As Kh", villainCards, null).toVillainCards())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Villain cards are required");
	}

	@Test
	void should_throw_exception_for_invalid_card() {
		assertThatThrownBy(() -> new EquityRequest("As Xh", "Qc Qd", null).toHeroCards())
			.isInstanceOf(IllegalArgumentException.class);
	}

}
