package com.gui.jhand.core.poker;

public record Card(Rank rank, Suit suit) {

	public static Card fromHandHistory(String card) {
		return new Card(Rank.fromSymbol(card.substring(0, 1)), Suit.fromSymbol(card.substring(1, 2)));
	}

	@Override
	public String toString() {
		return rank.getSymbol().concat(suit.getSymbol());
	}
}
