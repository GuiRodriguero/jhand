package com.gui.jhand.core.poker;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record Board(List<Card> cards) {

	public Board {
		cards = List.copyOf(cards);
	}

	public static Board of(Card... cards) {
		return new Board(List.of(cards));
	}

	public static Board fromString(String board) {
		String[] cardsAsStrings = board.trim().split("\\s+");

		return new Board(Arrays.stream(cardsAsStrings).map(Card::fromHandHistory).toList());
	}

	@Override
	public String toString() {
		return cards.stream().map(Card::toString).collect(Collectors.joining(" "));
	}

}
