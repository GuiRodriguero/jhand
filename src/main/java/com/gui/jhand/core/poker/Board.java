package com.gui.jhand.core.poker;

import java.util.Arrays;
import java.util.stream.Collectors;

public record Board(Card... cards) {

	public static Board fromString(String board) {
		String[] cardsAsStrings = board.trim().split("\\s+");

		return new Board(Arrays.stream(cardsAsStrings).map(Card::fromHandHistory).toArray(Card[]::new));
	}

	@Override
	public String toString() {
		return Arrays.stream(cards).map(Card::toString).collect(Collectors.joining(" "));
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Board board && Arrays.equals(cards, board.cards);
	}
}
