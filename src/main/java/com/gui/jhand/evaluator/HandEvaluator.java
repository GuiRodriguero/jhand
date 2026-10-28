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
class HandEvaluator {

	private static final int[][] SEVEN_CARDS_COMBINATIONS = { { 0, 1, 2, 3, 4 }, { 0, 1, 2, 3, 5 }, { 0, 1, 2, 3, 6 },
			{ 0, 1, 2, 4, 5 }, { 0, 1, 2, 4, 6 }, { 0, 1, 2, 5, 6 }, { 0, 1, 3, 4, 5 }, { 0, 1, 3, 4, 6 },
			{ 0, 1, 3, 5, 6 }, { 0, 1, 4, 5, 6 }, { 0, 2, 3, 4, 5 }, { 0, 2, 3, 4, 6 }, { 0, 2, 3, 5, 6 },
			{ 0, 2, 4, 5, 6 }, { 0, 3, 4, 5, 6 }, { 1, 2, 3, 4, 5 }, { 1, 2, 3, 4, 6 }, { 1, 2, 3, 5, 6 },
			{ 1, 2, 4, 5, 6 }, { 1, 3, 4, 5, 6 }, { 2, 3, 4, 5, 6 } };

	public static int evalFiveCards(Card c1, Card c2, Card c3, Card c4, Card c5) {
		return evalFiveCards(convertCard(c1), convertCard(c2), convertCard(c3), convertCard(c4), convertCard(c5));
	}

	public static int evalFiveCards(int c1, int c2, int c3, int c4, int c5) {
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

	public static int evalSevenCards(Card[] sevenCards) {
		if (sevenCards == null || sevenCards.length != 7) {
			throw new IllegalArgumentException("Invalid number of cards: expected 7");
		}

		int[] cards = new int[7];
		for (int i = 0; i < 7; i++) {
			cards[i] = convertCard(sevenCards[i]);
		}
		return evalSevenCards(cards);
	}

	public static int evalSevenCards(int[] sevenCards) {
		if (sevenCards == null || sevenCards.length != 7) {
			throw new IllegalArgumentException("Invalid number of cards: expected 7");
		}

		int bestScore = MAX_VALUE;
		for (int[] combo : SEVEN_CARDS_COMBINATIONS) {
			int score = evalFiveCards(sevenCards[combo[0]], sevenCards[combo[1]], sevenCards[combo[2]],
					sevenCards[combo[3]], sevenCards[combo[4]]);

			if (score < bestScore) {
				bestScore = score;
			}
		}

		return bestScore;
	}

	public static int evalSevenCards(PocketCards pocketCards, Board board) {
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
		return evalSevenCards(cards);
	}

}
