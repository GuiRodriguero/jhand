package com.gui.jhand.core.poker;

import static com.gui.jhand.core.poker.Rank.fromSymbol;

public record StartingHand(Rank high, Rank low, boolean suited) {

	public static StartingHand fromHandHistory(String cards) {
		Card card = Card.fromHandHistory(cards.substring(0, 2));
		Card card2 = Card.fromHandHistory(cards.substring(3, 5));

		Rank high;
		Rank low;

		if (card.rank().getValue() > card2.rank().getValue()) {
			high = card.rank();
			low = card2.rank();
		}
		else {
			high = card2.rank();
			low = card.rank();
		}

		return new StartingHand(high, low, card.suit() == card2.suit());
	}

	public static StartingHand fromDb(String cards) {
		Rank high = fromSymbol(cards.substring(0, 1));
		Rank low = fromSymbol(cards.substring(1, 2));

		return new StartingHand(high, low, cards.toLowerCase().endsWith("s"));
	}

	@Override
	public String toString() {
		String startingHand = high.getSymbol().concat(low.getSymbol());

		return high == low ? startingHand : startingHand.concat((suited ? "s" : "o"));
	}

}