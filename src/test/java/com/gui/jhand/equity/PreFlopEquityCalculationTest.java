package com.gui.jhand.equity;

import com.gui.jhand.TestBase;
import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

	@Test
	void should_draw_distinct_runout_cards_keeping_deck_as_permutation() {
		List<Card> remaining = remainingCards(
				collectDeadCards(fromHandHistory("As Ad"), fromHandHistory("Kc Kd"), Board.of()));
		Card[] deck = remaining.toArray(Card[]::new);
		Card[] runout = new Card[5];
		Random random = new Random(42);

		for (int i = 0; i < 1000; i++) {
			PreFlopEquityCalculation.drawRunout(deck, runout, random);

			assertThat(runout).doesNotHaveDuplicates().doesNotContainNull();
			assertThat(remaining).contains(runout);
			assertThat(deck).containsExactlyInAnyOrderElementsOf(remaining);
		}
	}

	@Test
	void should_draw_each_card_with_uniform_frequency() {
		List<Card> remaining = remainingCards(
				collectDeadCards(fromHandHistory("As Ad"), fromHandHistory("Kc Kd"), Board.of()));
		Card[] deck = remaining.toArray(Card[]::new);
		Card[] runout = new Card[5];
		Random random = new Random(7);
		Map<Card, Integer> frequency = new HashMap<>();
		int draws = 48_000;

		for (int i = 0; i < draws; i++) {
			PreFlopEquityCalculation.drawRunout(deck, runout, random);
			for (Card card : runout) {
				frequency.merge(card, 1, Integer::sum);
			}
		}

		// each of the 48 cards is expected in 5/48 of the draws (5000 times)
		double expected = draws * 5.0 / remaining.size();
		assertThat(frequency).hasSize(remaining.size());
		assertThat(frequency.values())
			.allSatisfy(count -> assertThat((double) count).isCloseTo(expected, within(expected * 0.06)));
	}

}
