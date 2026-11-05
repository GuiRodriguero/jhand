package com.gui.jhand.hand;

import com.gui.jhand.core.poker.Board;
import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.HandRank;
import com.gui.jhand.core.poker.PocketCards;
import com.gui.jhand.evaluator.HandEvaluator;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

import static com.gui.jhand.core.poker.HandRank.HIGH_CARD;
import static com.gui.jhand.core.poker.HandRank.PAIR;

@Component
class HandRankResolver {

	public HandRank calculatedRank(PocketCards heroCards, Board board) {
		if (board == null) {
			if (heroCards.cards().size() == 2) {
				return heroCards.cards().stream().map(Card::rank).distinct().count() > 1 ? HIGH_CARD : PAIR;
			}
			throw new IllegalArgumentException("Invalid number of cards to calculate rank");
		}

		Card[] cards = Stream.concat(heroCards.cards().stream(), board.cards().stream()).toArray(Card[]::new);
		return HandEvaluator.eval(cards).handRank();
	}

}