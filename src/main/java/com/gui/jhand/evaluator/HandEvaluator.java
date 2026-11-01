package com.gui.jhand.evaluator;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import lombok.NoArgsConstructor;

import static com.gui.jhand.evaluator.HandEvaluatorTables.FIVE_UNIQUE_CARDS_COMBINATIONS;
import static com.gui.jhand.evaluator.HandEvaluatorTables.FLUSH_TABLE;
import static com.gui.jhand.evaluator.HandEvaluatorUtils.*;
import static com.gui.jhand.evaluator.SenzeeHash.find;
import static java.lang.Integer.MAX_VALUE;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class HandEvaluator {

	private static final int[][] SEVEN_CARDS_COMBINATIONS = { { 0, 1, 2, 3, 4 }, { 0, 1, 2, 3, 5 }, { 0, 1, 2, 3, 6 },
			{ 0, 1, 2, 4, 5 }, { 0, 1, 2, 4, 6 }, { 0, 1, 2, 5, 6 }, { 0, 1, 3, 4, 5 }, { 0, 1, 3, 4, 6 },
			{ 0, 1, 3, 5, 6 }, { 0, 1, 4, 5, 6 }, { 0, 2, 3, 4, 5 }, { 0, 2, 3, 4, 6 }, { 0, 2, 3, 5, 6 },
			{ 0, 2, 4, 5, 6 }, { 0, 3, 4, 5, 6 }, { 1, 2, 3, 4, 5 }, { 1, 2, 3, 4, 6 }, { 1, 2, 3, 5, 6 },
			{ 1, 2, 4, 5, 6 }, { 1, 3, 4, 5, 6 }, { 2, 3, 4, 5, 6 } };

	private static final int[][] SIX_CARDS_COMBINATIONS = { { 0, 1, 2, 3, 4 }, { 0, 1, 2, 3, 5 }, { 0, 1, 2, 4, 5 },
			{ 0, 1, 3, 4, 5 }, { 0, 2, 3, 4, 5 }, { 1, 2, 3, 4, 5 } };

	public static HandEvaluationResult eval(Card[] cardsToEval) {
		validate(cardsToEval);
		int[] cards = convertCardsToInt(cardsToEval);

		int score = switch (cards.length) {
			case 5 -> evalFiveCards(cards[0], cards[1], cards[2], cards[3], cards[4]);
			case 6 -> extractBestScoreFromCards(cards, SIX_CARDS_COMBINATIONS);
			case 7 -> extractBestScoreFromCards(cards, SEVEN_CARDS_COMBINATIONS);
			default -> throw new IllegalArgumentException("Unexpected length: " + cards.length);
		};

		return HandEvaluationResult.of(score);
	}

	private static void validate(Card[] cards) {
		if (cards == null || (cards.length != 5 && cards.length != 6 && cards.length != 7)) {
			throw new IllegalArgumentException("Invalid number of cards to evaluate, should be either 5, 6 or 7");
		}
	}

	private static int[] convertCardsToInt(Card[] cardsToEval) {
		int numberOfCards = cardsToEval.length;
		int[] cards = new int[numberOfCards];
		for (int i = 0; i < numberOfCards; i++) {
			cards[i] = convertCard(cardsToEval[i]);
		}
		return cards;
	}

	private static int evalFiveCards(int c1, int c2, int c3, int c4, int c5) {
		int q = (c1 | c2 | c3 | c4 | c5) >>> 16;

		if (allSuitsAreEqual(c1, c2, c3, c4, c5)) {
			return FLUSH_TABLE[q];
		}

		short uniqueScore = FIVE_UNIQUE_CARDS_COMBINATIONS[q];
		if (uniqueScore != 0) { // Straights and High Cards
			return uniqueScore;
		}

		int product = getPrimeRepresentation(c1) * getPrimeRepresentation(c2) * getPrimeRepresentation(c3)
				* getPrimeRepresentation(c4) * getPrimeRepresentation(c5);
		return find(product);
	}

	public static HandEvaluationResult evalSevenCards(PocketCards pocketCards, Board board) {
		if (pocketCards == null || pocketCards.cards() == null || pocketCards.cards().size() != 2) {
			throw new IllegalArgumentException("Invalid pocket cards: expected 2");
		}
		if (board == null || board.cards() == null || board.cards().length != 5) {
			throw new IllegalArgumentException("Invalid board: expected 5 cards");
		}

		int[] cards = new int[7];
		cards[0] = convertCard(pocketCards.cards().get(0));
		cards[1] = convertCard(pocketCards.cards().get(1));
		for (int i = 0; i < 5; i++) {
			cards[i + 2] = convertCard(board.cards()[i]);
		}
		int score = extractBestScoreFromCards(cards, SEVEN_CARDS_COMBINATIONS);
		return HandEvaluationResult.of(score);
	}

	private static int extractBestScoreFromCards(int[] cards, int[][] combinations) {
		int bestScore = MAX_VALUE;
		for (int[] combo : combinations) {
			int score = evalFiveCards(cards[combo[0]], cards[combo[1]], cards[combo[2]], cards[combo[3]],
					cards[combo[4]]);

			if (score < bestScore) {
				bestScore = score;
			}
		}

		return bestScore;
	}

}
