package com.gui.jhand.equity;

import com.gui.jhand.core.poker.Card;
import com.gui.jhand.core.poker.Rank;
import com.gui.jhand.core.poker.Suit;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class Deck {

	private static final List<Card> FULL_DECK = new ArrayList<>(52);

	static {
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				FULL_DECK.add(new Card(rank, suit));
			}
		}
	}

	public static List<Card> remainingCards(Set<Card> deadCards) {
		return FULL_DECK.stream().filter(card -> !deadCards.contains(card)).toList();
	}

}
