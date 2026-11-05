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
import static com.gui.jhand.equity.EquityEvaluationWinner.resolveWinner;
import static com.gui.jhand.evaluator.HandEvaluator.eval;
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
		List<Card> currentBoard = board.cards();
		int boardLength = currentBoard.size();

		EquityCalculationStrategy strategy = strategyMap.get(boardLength);
		if (strategy == null) {
			throw new IllegalArgumentException("Unsupported board length: " + boardLength);
		}
		return strategy.calculate(hero, villain, currentBoard, remainingCardsOnDeck);
	}

	static EquityEvaluationWinner evaluateScenario(PocketCards hero, PocketCards villain, List<Card> currentBoard,
			Card... communityCardsToEvaluate) {
		Card[] fullBoard = new Card[currentBoard.size() + communityCardsToEvaluate.length];
		for (int i = 0; i < currentBoard.size(); i++) {
			fullBoard[i] = currentBoard.get(i);
		}
		System.arraycopy(communityCardsToEvaluate, 0, fullBoard, currentBoard.size(), communityCardsToEvaluate.length);

		Card[] heroSeven = buildSevenCardsFromBoard(hero, fullBoard);
		Card[] villainSeven = buildSevenCardsFromBoard(villain, fullBoard);

		int heroScore = eval(heroSeven).score();
		int villainScore = eval(villainSeven).score();

		return resolveWinner(heroScore, villainScore);
	}

	static Card[] buildSevenCardsFromBoard(PocketCards pocket, Card[] board) {
		return new Card[] { pocket.cards().get(0), pocket.cards().get(1), board[0], board[1], board[2], board[3],
				board[4] };
	}

}
