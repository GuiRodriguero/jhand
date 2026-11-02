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

class FlopEquityCalculationTest extends TestBase {

	private FlopEquityCalculation flopEquityCalculation;

	@Override
	public void init() {
		flopEquityCalculation = new FlopEquityCalculation();
	}

	@Test
	void should_get_supported_board_length() {
		assertThat(flopEquityCalculation.getSupportedBoardLength()).isEqualTo(3);
	}

	@Test
	void should_calculate() {
		PocketCards hero = fromHandHistory("Kd Qd");
		PocketCards villain = fromHandHistory("Ad 8h");
		Board flop = Board.fromString("Kh Td 9d");
		List<Card> remaining = remainingCards(collectDeadCards(hero, villain, flop));

		assertThat(flopEquityCalculation.calculate(hero, villain, flop.cards(), remaining))
			.isEqualTo(new EquityResult(0.8777777777777778, 0.12222222222222222, 0, 869, 121, 0, 990));
	}

}
