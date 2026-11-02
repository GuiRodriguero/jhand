package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.PocketCards;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

class EquityCalculatorTest {

	@Test
	void should_calculate_river_showdown_hero_win() {
		PocketCards hero = PocketCards.fromHandHistory("Ah Kd");
		PocketCards villain = PocketCards.fromHandHistory("Qc Qd");
		Board board = Board.fromString("Ad Ks 7s 4d 2s");

		assertThat(EquityCalculator.calculate(hero, villain, board))
			.isEqualTo(new EquityResult(1.0, 0.0, 0.0, 1, 0, 0, 1));
	}

	@Test
	void should_calculate_turn_equity() {
		PocketCards hero = PocketCards.fromHandHistory("Ah Kh");
		PocketCards villain = PocketCards.fromHandHistory("9s 9d");
		Board board = Board.fromString("Ks 9c 4h 2c");

		assertThat(EquityCalculator.calculate(hero, villain, board))
			.isEqualTo(new EquityResult(0.0, 1.0, 0.0, 0, 44, 0, 44));
	}

	@Test
	void should_calculate_flop_equity() {
		PocketCards hero = PocketCards.fromHandHistory("Ts Tc");
		PocketCards villain = PocketCards.fromHandHistory("Ah Kd");
		Board board = Board.fromString("7s 4d 2s");

		EquityResult result = EquityCalculator.calculate(hero, villain, board);

		// 45 remaining cards => C(45, 2) = 990 scenarios
		assertThat(result.totalScenarios()).isEqualTo(990);
		// Overpair vs 2 overcards has ~76% equity on dry board
		assertThat(result.heroEquity()).isCloseTo(0.76, within(0.01));
		assertThat(result.villainEquity()).isCloseTo(0.24, within(0.01));
	}

	@Test
	void should_handle_tie_split_pot() {
		PocketCards hero = PocketCards.fromHandHistory("Ah 2c");
		PocketCards villain = PocketCards.fromHandHistory("Ad 3s");
		Board royalFlushBoard = Board.fromString("As Ks Qs Js Ts");

		assertThat(EquityCalculator.calculate(hero, villain, royalFlushBoard))
			.isEqualTo(new EquityResult(0.5, 0.5, 1.0, 0, 0, 1, 1));
	}

}
