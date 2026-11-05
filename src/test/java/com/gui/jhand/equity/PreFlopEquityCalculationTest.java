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
import static org.assertj.core.api.Assertions.within;

class PreFlopEquityCalculationTest extends TestBase {

	private PreFlopEquityCalculation preFlopEquityCalculation;

	@Override
	public void init() {
		preFlopEquityCalculation = new PreFlopEquityCalculation(10000);
	}

	@Test
	void should_get_supported_board_length() {
		assertThat(preFlopEquityCalculation.getSupportedBoardLength()).isEqualTo(0);
	}

	@Test
	void should_calculate() {
		PocketCards hero = fromHandHistory("As Ad");
		PocketCards villain = fromHandHistory("Kc Kd");
		List<Card> remaining = remainingCards(collectDeadCards(hero, villain, Board.of()));

		EquityResult equity = preFlopEquityCalculation.calculate(hero, villain, null, remaining);

		assertThat(equity.heroEquity()).isEqualTo(0.82, within(0.02));
		assertThat(equity.villainEquity()).isEqualTo(0.18, within(0.02));
	}

}
