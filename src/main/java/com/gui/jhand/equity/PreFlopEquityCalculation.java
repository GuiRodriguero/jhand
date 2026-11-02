package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.gui.jhand.equity.EquityCalculator.evaluateScenario;

@Component
final class PreFlopEquityCalculation implements EquityCalculationStrategy {

	private final int iterations;

	public PreFlopEquityCalculation(@Value("${poker.monte-carlo.iterations:100000}") int iterations) {
		this.iterations = iterations;
	}

	@Override
	public int getSupportedBoardLength() {
		return 0;
	}

	@Override
	public EquityResult calculate(PocketCards hero, PocketCards villain, Card[] board, List<Card> remaining) {
		EquityCounter counter = new EquityCounter();
		List<Card> deckCopy = new ArrayList<>(remaining);
		ThreadLocalRandom random = ThreadLocalRandom.current();

		for (int i = 0; i < iterations; i++) {
			Collections.shuffle(deckCopy, random);
			Card[] fullBoard = new Card[] { deckCopy.get(0), deckCopy.get(1), deckCopy.get(2), deckCopy.get(3),
					deckCopy.get(4) };
			EquityEvaluationWinner winner = evaluateScenario(hero, villain, fullBoard);
			counter.add(winner);
		}

		return counter.toResult();
	}

}
