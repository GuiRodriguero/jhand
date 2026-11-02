package com.gui.jhand.hand;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.PocketCards;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gui.jhand.core.poker.HandRank.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandRankResolverTest {

	private final HandRankResolver resolver = new HandRankResolver();

	private final PocketCards heroCards = PocketCards.fromHandHistory("As Ks");

	@Test
	void should_evaluate_boards_on_each_street() {
		for (String board : List.of("Qs Js Ts", "Qs Js Ts 2h", "Qs Js Ts 2h 3c")) {
			assertThat(resolver.calculatedRank(heroCards, Board.fromString(board))).isEqualTo(ROYAL_FLUSH);
		}
	}

	@Test
	void should_classify_valid_hero_cards_before_flop() {
		assertThat(resolver.calculatedRank(heroCards, null)).isEqualTo(HIGH_CARD);
		assertThat(resolver.calculatedRank(PocketCards.fromHandHistory("As Ah"), null)).isEqualTo(PAIR);
	}

	@Test
	void should_reject_invalid_cards() {
		assertThatThrownBy(() -> resolver.calculatedRank(new PocketCards(List.of()), null))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("Invalid number of cards to calculate rank");
		assertThatThrownBy(() -> resolver.calculatedRank(PocketCards.fromHandHistory("As"), null))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("Invalid number of cards to calculate rank");
		assertThatThrownBy(() -> resolver.calculatedRank(PocketCards.fromHandHistory("As Ks Qs"), null))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("Invalid number of cards to calculate rank");
		assertThatThrownBy(() -> resolver.calculatedRank(heroCards, Board.fromString("Qs Js")))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("Invalid number of cards to evaluate, should be either 5, 6 or 7");
		assertThatThrownBy(() -> resolver.calculatedRank(heroCards, Board.fromString("Qs Js Ts 2h 3c 4d")))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage("Invalid number of cards to evaluate, should be either 5, 6 or 7");
	}

	@Test
	void should_evaluate_best_hand_using_all_available_cards() {
		assertThat(resolver.calculatedRank(PocketCards.fromHandHistory("2h 3d"), Board.fromString("As Ks Qs Js Ts")))
			.isEqualTo(ROYAL_FLUSH);
	}

}