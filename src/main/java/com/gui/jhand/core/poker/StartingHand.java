package com.gui.jhand.core.poker;

public record StartingHand(Card card1, Card card2) {

	public static StartingHand fromHandHistory(String cards) {
		Card card = Card.fromHandHistory(cards.substring(0, 2));
		Card card2 = Card.fromHandHistory(cards.substring(3, 5));

		if (card.rank().getValue() >= card2.rank().getValue()) {
			return new StartingHand(card, card2);
		}
		return new StartingHand(card2, card);
	}

	public static StartingHand fromDb(String cards) {
		return new StartingHand(Card.fromHandHistory(cards.substring(0, 2)),
				Card.fromHandHistory(cards.substring(3, 5)));
	}

	@Override
	public String toString() {
		return card1.toString() + " " + card2.toString();
	}

}