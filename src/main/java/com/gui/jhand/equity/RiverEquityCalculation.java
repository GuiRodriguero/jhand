package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.equity.EquityEvaluationWinner.*;
import static com.gui.jhand.equity.ShowdownEvaluator.evaluate;

@Component
final class RiverEquityCalculation implements EquityCalculationStrategy {

	@Override
	public int getSupportedBoardLength() {
		return 5;
	}

	@Override
	public EquityResult calculate(PocketCards hero, PocketCards villain, List<Card> board, List<Card> remaining) {
		EquityEvaluationWinner winner = evaluate(hero, villain, board);
		return EquityResult.of(winner == HERO ? 1 : 0, winner == VILLAIN ? 1 : 0, winner == TIE ? 1 : 0);
	}

}
