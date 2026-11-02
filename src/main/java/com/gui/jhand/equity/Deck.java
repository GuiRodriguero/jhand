package com.gui.jhand.equity;

import com.gui.jhand.core.poker.*;
import lombok.NoArgsConstructor;

import java.util.*;

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

	static List<Card> remainingCards(Set<Card> deadCards) {
		return FULL_DECK.stream().filter(card -> !deadCards.contains(card)).toList();
	}

	static Set<Card> collectDeadCards(PocketCards hero, PocketCards villain, Board board) {
		Set<Card> deadCards = new HashSet<>();
		deadCards.addAll(hero.cards());
		deadCards.addAll(villain.cards());
		deadCards.addAll(Arrays.asList(board.cards()));
		return deadCards;
	}

}
