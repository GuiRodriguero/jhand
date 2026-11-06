package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

import static com.gui.jhand.equity.ShowdownEvaluator.evaluate;

@Component
final class PreFlopEquityCalculation implements EquityCalculationStrategy {

	private static final int RUNOUT_SIZE = 5;

	private final int iterations;

	public PreFlopEquityCalculation(@Value("${poker.monte-carlo.iterations:100000}") int iterations) {
		this.iterations = iterations;
	}

	@Override
	public int getSupportedBoardLength() {
		return 0;
	}

	@Override
	public EquityResult calculate(PocketCards hero, PocketCards villain, List<Card> board, List<Card> remaining) {
		EquityCounter counter = new EquityCounter();
		Card[] deck = remaining.toArray(Card[]::new);
		Card[] runout = new Card[RUNOUT_SIZE];
		ThreadLocalRandom random = ThreadLocalRandom.current();

		for (int i = 0; i < iterations; i++) {
			drawRunout(deck, runout, random);
			counter.add(evaluate(hero, villain, List.of(), runout));
		}

		return counter.toResult();
	}

	static void drawRunout(Card[] deck, Card[] runout, RandomGenerator random) {
		for (int k = 0; k < runout.length; k++) {
			int j = k + random.nextInt(deck.length - k);
			Card swapped = deck[k];
			deck[k] = deck[j];
			deck[j] = swapped;
			runout[k] = deck[k];
		}
	}

}
