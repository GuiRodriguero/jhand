package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.equity.ShowdownEvaluator.evaluate;

@Component
final class FlopEquityCalculation implements EquityCalculationStrategy {

	@Override
	public int getSupportedBoardLength() {
		return 3;
	}

	@Override
	public EquityResult calculate(PocketCards hero, PocketCards villain, List<Card> board, List<Card> remaining) {
		EquityCounter counter = new EquityCounter();
		int size = remaining.size();

		for (int i = 0; i < size - 1; i++) {
			Card turnCard = remaining.get(i);
			for (int j = i + 1; j < size; j++) {
				Card riverCard = remaining.get(j);
				EquityEvaluationWinner winner = evaluate(hero, villain, board, turnCard, riverCard);
				counter.add(winner);
			}
		}
		return counter.toResult();
	}

}
