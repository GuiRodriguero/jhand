package com.gui.jhand.equity;

import com.gui.jhand.TestBase;
import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gui.jhand.core.poker.PocketCards.fromHandHistory;
import static com.gui.jhand.equity.Deck.collectDeadCards;
import static com.gui.jhand.equity.Deck.remainingCards;
import static org.assertj.core.api.Assertions.assertThat;

class RiverEquityCalculationTest extends TestBase {

	private RiverEquityCalculation riverEquityCalculation;

	@Override
	public void init() {
		riverEquityCalculation = new RiverEquityCalculation();
	}

	@Test
	void should_get_supported_board_length() {
		assertThat(riverEquityCalculation.getSupportedBoardLength()).isEqualTo(5);
	}

	@Test
	void should_calculate() {
		PocketCards hero = fromHandHistory("Kd Qd");
		PocketCards villain = fromHandHistory("Ad 8h");
		Board board = Board.fromString("Kh Td 9d 3d Js");
		List<Card> remaining = remainingCards(collectDeadCards(hero, villain, board));

		assertThat(riverEquityCalculation.calculate(hero, villain, board.cards(), remaining))
			.isEqualTo(new EquityResult(1.0, 0.0, 0, 1, 0, 0, 1));
	}

}
