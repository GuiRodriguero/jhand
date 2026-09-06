package com.gui.jhand.core.poker;

public record PocketCards(Card card1, Card card2) {

	public static PocketCards fromHandHistory(String cards) {
		Card card = Card.fromHandHistory(cards.substring(0, 2));
		Card card2 = Card.fromHandHistory(cards.substring(3, 5));

		if (card.rank().getValue() >= card2.rank().getValue()) {
			return new PocketCards(card, card2);
		}
		return new PocketCards(card2, card);
	}

	public static PocketCards fromDb(String cards) {
		return new PocketCards(Card.fromHandHistory(cards.substring(0, 2)),
				Card.fromHandHistory(cards.substring(3, 5)));
	}

	@Override
	public String toString() {
		return card1.toString() + " " + card2.toString();
	}

}