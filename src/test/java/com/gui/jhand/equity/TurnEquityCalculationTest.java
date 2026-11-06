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

class TurnEquityCalculationTest extends TestBase {

	private TurnEquityCalculation turnEquityCalculation;

	@Override
	public void init() {
		turnEquityCalculation = new TurnEquityCalculation();
	}

	@Test
	void should_get_supported_board_length() {
		assertThat(turnEquityCalculation.getSupportedBoardLength()).isEqualTo(4);
	}

	@Test
	void should_calculate() {
		PocketCards hero = fromHandHistory("Kd Qd");
		PocketCards villain = fromHandHistory("Ad 8h");
		Board board = Board.fromString("Kh Td 9d 3d");
		List<Card> remaining = remainingCards(collectDeadCards(hero, villain, board));

		assertThat(turnEquityCalculation.calculate(hero, villain, board.cards(), remaining))
			.isEqualTo(new EquityResult(0.8636363636363636, 0.13636363636363635, 0, 38, 6, 0, 44));
	}

}
