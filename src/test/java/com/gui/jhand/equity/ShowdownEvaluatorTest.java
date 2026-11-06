package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.PocketCards;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gui.jhand.core.poker.Card.fromHandHistory;
import static com.gui.jhand.equity.EquityEvaluationWinner.*;
import static org.assertj.core.api.Assertions.assertThat;

class ShowdownEvaluatorTest {

	private final PocketCards hero = PocketCards.fromHandHistory("Ah Kd");

	private final PocketCards villain = PocketCards.fromHandHistory("Qc Qd");

	@Test
	void should_evaluate_hero_win_with_complete_board() {
		assertThat(ShowdownEvaluator.evaluate(hero, villain, Board.fromString("Ad Ks 7s 4d 2s").cards()))
			.isEqualTo(HERO);
	}

	@Test
	void should_evaluate_villain_win_with_runout() {
		assertThat(ShowdownEvaluator.evaluate(hero, villain, Board.fromString("7s 4d 2s").cards(),
				fromHandHistory("Qs"), fromHandHistory("3h")))
			.isEqualTo(VILLAIN);
	}

	@Test
	void should_evaluate_tie_when_board_plays() {
		assertThat(ShowdownEvaluator.evaluate(hero, villain, List.of(), fromHandHistory("As"), fromHandHistory("Ks"),
				fromHandHistory("Qs"), fromHandHistory("Js"), fromHandHistory("Ts")))
			.isEqualTo(TIE);
	}

}
