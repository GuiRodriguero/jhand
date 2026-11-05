package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.gui.jhand.equity.Deck.collectDeadCards;
import static com.gui.jhand.equity.Deck.remainingCards;
import static java.util.stream.Collectors.toMap;

@Service
class EquityCalculator {

	private final Map<Integer, EquityCalculationStrategy> strategyMap;

	EquityCalculator(List<EquityCalculationStrategy> strategies) {
		this.strategyMap = strategies.stream()
			.collect(toMap(EquityCalculationStrategy::getSupportedBoardLength, Function.identity()));
	}

	EquityResult calculate(PocketCards hero, PocketCards villain, Board board) {
		List<Card> remainingCardsOnDeck = remainingCards(collectDeadCards(hero, villain, board));
		int boardLength = board.cards().size();

		EquityCalculationStrategy strategy = strategyMap.get(boardLength);
		if (strategy == null) {
			throw new IllegalArgumentException("Unsupported board length: " + boardLength);
		}
		return strategy.calculate(hero, villain, board.cards(), remainingCardsOnDeck);
	}

}
