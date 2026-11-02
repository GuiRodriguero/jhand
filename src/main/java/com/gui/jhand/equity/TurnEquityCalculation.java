package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gui.jhand.equity.EquityCalculator.evaluateScenario;

@Component
final class TurnEquityCalculation implements EquityCalculationStrategy {

	@Override
	public int getSupportedBoardLength() {
		return 4;
	}

	@Override
	public EquityResult calculate(PocketCards hero, PocketCards villain, Card[] board, List<Card> remaining) {
		EquityCounter counter = new EquityCounter();
		remaining.stream().map(riverCard -> evaluateScenario(hero, villain, board, riverCard)).forEach(counter::add);
		return counter.toResult();
	}

}
