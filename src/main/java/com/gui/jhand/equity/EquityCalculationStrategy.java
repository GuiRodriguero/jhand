package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;

import java.util.List;

sealed interface EquityCalculationStrategy
		permits PreFlopEquityCalculation, FlopEquityCalculation, TurnEquityCalculation, RiverEquityCalculation {

	int getSupportedBoardLength();

	EquityResult calculate(PocketCards hero, PocketCards villain, Card[] board, List<Card> remaining);

}
