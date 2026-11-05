package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.PocketCards;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.gui.jhand.equity.EquityEvaluationWinner.resolveWinner;
import static com.gui.jhand.evaluator.HandEvaluator.eval;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class ShowdownEvaluator {

	static EquityEvaluationWinner evaluate(PocketCards hero, PocketCards villain, List<Card> board, Card... runout) {
		Card[] communityCards = communityCards(board, runout);

		int heroScore = eval(withPocketCards(hero, communityCards)).score();
		int villainScore = eval(withPocketCards(villain, communityCards)).score();

		return resolveWinner(heroScore, villainScore);
	}

	private static Card[] communityCards(List<Card> board, Card... runout) {
		Card[] communityCards = new Card[board.size() + runout.length];
		for (int i = 0; i < board.size(); i++) {
			communityCards[i] = board.get(i);
		}
		System.arraycopy(runout, 0, communityCards, board.size(), runout.length);
		return communityCards;
	}

	private static Card[] withPocketCards(PocketCards pocket, Card[] communityCards) {
		Card[] cards = new Card[pocket.cards().size() + communityCards.length];
		for (int i = 0; i < pocket.cards().size(); i++) {
			cards[i] = pocket.cards().get(i);
		}
		System.arraycopy(communityCards, 0, cards, pocket.cards().size(), communityCards.length);
		return cards;
	}

}
