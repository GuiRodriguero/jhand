package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gui.jhand.equity.EquityEvaluationWinner.*;
import static com.gui.jhand.evaluator.HandEvaluator.eval;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class EquityCalculator {

	public static EquityResult calculate(PocketCards hero, PocketCards villain, Board board) {
		Set<Card> deadCards = collectDeadCards(hero, villain, board);
		List<Card> remaining = Deck.remainingCards(deadCards);
		Card[] currentBoard = board.cards();

		return switch (currentBoard.length) {
			case 5 -> evaluateRiver(hero, villain, currentBoard);
			case 4 -> evaluateTurn(hero, villain, currentBoard, remaining);
			case 3 -> evaluateFlop(hero, villain, currentBoard, remaining);
			default -> throw new IllegalArgumentException("Board must have 3, 4, or 5 cards");
		};
	}

	private static Set<Card> collectDeadCards(PocketCards hero, PocketCards villain, Board board) {
		Set<Card> deadCards = new HashSet<>();
		deadCards.addAll(hero.cards());
		deadCards.addAll(villain.cards());
		deadCards.addAll(Arrays.asList(board.cards()));
		return deadCards;
	}

	private static EquityResult evaluateRiver(PocketCards hero, PocketCards villain, Card[] board) {
		EquityEvaluationWinner winner = evaluateScenario(hero, villain, board);
		return EquityResult.of(winner == HERO ? 1 : 0, winner == VILLAIN ? 1 : 0, winner == TIE ? 1 : 0);
	}

	private static EquityResult evaluateTurn(PocketCards hero, PocketCards villain, Card[] board,
			List<Card> remaining) {
		EquityCounter counter = new EquityCounter();
		remaining.stream().map(riverCard -> evaluateScenario(hero, villain, board, riverCard)).forEach(counter::add);
		return counter.toResult();
	}

	private static EquityResult evaluateFlop(PocketCards hero, PocketCards villain, Card[] board,
			List<Card> remaining) {
		EquityCounter counter = new EquityCounter();
		int size = remaining.size();

		for (int i = 0; i < size - 1; i++) {
			Card turnCard = remaining.get(i);
			for (int j = i + 1; j < size; j++) {
				Card riverCard = remaining.get(j);
				EquityEvaluationWinner winner = evaluateScenario(hero, villain, board, turnCard, riverCard);
				counter.add(winner);
			}
		}

		return counter.toResult();
	}

	private static EquityEvaluationWinner evaluateScenario(PocketCards hero, PocketCards villain, Card[] currentBoard,
			Card... communityCardsToEvaluate) {
		Card[] fullBoard = new Card[currentBoard.length + communityCardsToEvaluate.length];
		System.arraycopy(currentBoard, 0, fullBoard, 0, currentBoard.length);
		System.arraycopy(communityCardsToEvaluate, 0, fullBoard, currentBoard.length, communityCardsToEvaluate.length);

		Card[] heroSeven = buildSevenCardsFromBoard(hero, fullBoard);
		Card[] villainSeven = buildSevenCardsFromBoard(villain, fullBoard);

		int heroScore = eval(heroSeven).score();
		int villainScore = eval(villainSeven).score();

		return resolveWinner(heroScore, villainScore);
	}

	private static Card[] buildSevenCardsFromBoard(PocketCards pocket, Card[] board) {
		return new Card[] { pocket.cards().get(0), pocket.cards().get(1), board[0], board[1], board[2], board[3],
				board[4] };
	}

}
